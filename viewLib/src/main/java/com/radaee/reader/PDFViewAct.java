package com.radaee.reader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.radaee.pdf.Document;
import com.radaee.util.PDFAssetStream;
import com.radaee.util.PDFHttpStream;
import com.radaee.Broadcast.MyBroadcast;
import com.radaee.pdf.Global;
import com.radaee.pdf.Page.Annotation;
import com.radaee.view.VPage;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;


public class PDFViewAct extends Activity implements PDFLayoutView.PDFLayoutListener {
	static protected Document ms_tran_doc;
	static protected String pdfWorkPath;
	static protected String pdfName;
	static private int m_tmp_index = 0;
	private PDFAssetStream m_asset_stream = null;
	private PDFHttpStream m_http_stream = null;
	private Document m_doc = null;
	private RelativeLayout m_layout = null;
	private PDFLayoutView m_view = null;
	private PDFViewController m_controller = null;
	private boolean m_modified = false;
	private boolean need_save_doc = false;
	private TextToSpeech mTts;
	 String strTranslatedText = null;
	private UploadTask uploadTask;

	public final static int CAMERA = 100;
	protected static final int REQ_TTS_STATUS_CHECK = 0;
	private StorageReference mStorageRef;
	public onImageViewCommand mCallback;
	public ShowImageToNote showImageToNote;
	Uri imgUri;

	FirebaseStorage storage;
	StorageReference storageReference;

	public void setOnCommandListenter(onImageViewCommand callback, ShowImageToNote showImageToNote) {

		mCallback = callback;
		this.showImageToNote = showImageToNote;
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO 得到PDF的URI並用回呼給PDFLayoutView 顯示出來PDF
		if (requestCode == CAMERA) {
			if (resultCode == Activity.RESULT_OK) {
				Uri uri = data.getData();
				mCallback.onShowImage(getRealPathFromURI(uri));
			}
		}else if(requestCode == PDFLayoutView.ImageNote)
		{
			if(requestCode == PDFLayoutView.ImageNote)
			{
				imgUri = data.getData();
				Bitmap photobmp = null;
				try {
					photobmp = BitmapFactory.decodeStream(getContentResolver().openInputStream(imgUri), null, null);
				} catch (FileNotFoundException e) {
					Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
				}
				showImageToNote.ShowImageToNote(photobmp);
			}else{
				Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
			}
		}
	}

	// Android4.4以上的URI路徑找尋方法
	private String getRealPathFromURI(Uri contentURI) {
		String result;

		Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

		if (cursor == null) {
			result = contentURI.getPath();

		} else {
			cursor.moveToFirst();
			int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
			result = cursor.getString(index);
			cursor.close();
		}

		return result;
	}

	private void onFail(String msg)// treat open failed.
	{
		m_doc.Close();
		m_doc = null;
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		finish();
	}

	private final void ProcessOpenResult(int ret) {

		switch (ret) {
		case -1:// need input password
			onFail("打開失敗：密碼無效");
			break;
		case -2:// unknown encryption
			onFail("打開失敗：未知加密");
			break;
		case -3:// damaged or invalid format
			onFail("打開失敗：損壞或無效的PDF文件");
			break;
		case -10:// access denied or invalid file path
			onFail("打開失敗：路徑無效");
			break;
		case 0:// succeeded, and continue
			m_view.PDFOpen(m_doc, this);

			m_controller = new PDFViewController(m_layout, m_view);
			break;
		default:// unknown error
			onFail("打開失敗：未知錯誤");
			break;
		}
	}

	class MyPDFFontDel implements Document.PDFFontDelegate {
		@Override
		public String GetExtFont(String collection, String fname, int flag, int[] ret_flags) {
			Log.i("ExtFont", fname);
			return null;
		}
	}

	private MyPDFFontDel m_font_del = new MyPDFFontDel();

	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		mStorageRef = FirebaseStorage.getInstance().getReference();	//初始化Firebase
		super.onCreate(savedInstanceState);
		// plz set this line to Activity in AndroidManifes.xml:
		// android:configChanges="orientation|keyboardHidden|screenSize"
		// otherwise, APP shall destroy this Activity and re-create a new
		// Activity when rotate.
		Global.Init(this);
		m_layout = (RelativeLayout) LayoutInflater.from(this).inflate(com.radaee.viewlib.R.layout.mypdf_layout, null);
		m_view = (PDFLayoutView) m_layout.findViewById(com.radaee.viewlib.R.id.pdf_view);
		m_view.setActivity(this);
		Intent intent = getIntent();
		String bmp_format = intent.getStringExtra("BMPFormat");
		if (bmp_format != null) {
			if (bmp_format.compareTo("RGB_565") == 0)
				m_view.PDFSetBmpFormat(Bitmap.Config.RGB_565);
			else if (bmp_format.compareTo("ARGB_4444") == 0)
				m_view.PDFSetBmpFormat(Bitmap.Config.ARGB_4444);
		}
		if (ms_tran_doc != null) {
			m_doc = ms_tran_doc;
			ms_tran_doc = null;
			m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));// set
																							// temporary
																							// cache
																							// for
																							// editing.
			m_tmp_index++;
			m_view.PDFOpen(m_doc, this);
			m_controller = new PDFViewController(m_layout, m_view);
			need_save_doc = true;
		} else {
			String pdf_asset = intent.getStringExtra("PDFAsset");
			String pdf_path = intent.getStringExtra("PDFPath");

			String pdf_pswd = intent.getStringExtra("PDFPswd");
			String pdf_http = intent.getStringExtra("PDFHttp");
			if (pdf_http != null && pdf_http != "") {
				m_http_stream = new PDFHttpStream();
				m_http_stream.open(pdf_http);
				m_doc = new Document();
				int ret = m_doc.OpenStream(m_http_stream, pdf_pswd);
				ProcessOpenResult(ret);
			} else if (pdf_asset != null && pdf_asset != "") {
				m_asset_stream = new PDFAssetStream();
				m_asset_stream.open(getAssets(), pdf_asset);

				m_doc = new Document();
				int ret = m_doc.OpenStream(m_asset_stream, pdf_pswd);

				ProcessOpenResult(ret);
			} else if (pdf_path != null && pdf_path != "") {
				m_doc = new Document();
				int ret = m_doc.Open(pdf_path, pdf_pswd);

				m_doc.SetCache(String.format("%s/temp%08x.dat", Global.tmp_path, m_tmp_index));// set
																								// temporary
																								// cache
																								// for
																								// editing.
				m_tmp_index++;
				m_doc.SetFontDel(m_font_del);
				ProcessOpenResult(ret);
			}
		}
		setContentView(m_layout);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (m_doc == null)
			m_doc = m_view.PDFGetDoc();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		m_view.BundleSavePos(savedInstanceState);
		if (need_save_doc && m_doc != null) {
			Document.BundleSave(savedInstanceState, m_doc);// save Document
															// object
			m_doc = null;
		}
	}

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (m_doc == null) {
			m_doc = Document.BundleRestore(savedInstanceState);// restore
																// Document
																// object
			m_view.PDFOpen(m_doc, this);
			m_controller = new PDFViewController(m_layout, m_view);
			need_save_doc = true;
		}
		m_view.BundleRestorePos(savedInstanceState);
	}

	@Override
	public void onBackPressed() {
		if (m_controller == null || m_controller.OnBackPressed()) {
			if (m_modified) {
				TextView txtView = new TextView(this);
				txtView.setText("文件修改\r\n是否要保存?\n"+pdfName);
				new AlertDialog.Builder(this).setTitle("").setView(txtView)
						.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {

								m_doc.Save();

								if(pdfWorkPath!=null&&pdfWorkPath!=""&&PDFLayoutView.WordNum_btn==false){

										File SDCardpath = Environment.getExternalStorageDirectory();
										Uri file = Uri.fromFile(new File(pdfWorkPath+"/" + pdfName));
										StorageReference riversRef = mStorageRef.child(PDFLayoutView.WordNum+"/"+file.getLastPathSegment());
										uploadTask = riversRef.putFile(file);
										uploadTask.addOnFailureListener(new OnFailureListener() {
											@Override
											public void onFailure(@NonNull Exception exception) {
												// Handle unsuccessful uploads
												Toast.makeText(getBaseContext(), "請開啟網路", Toast.LENGTH_SHORT).show();
											}
										}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
											@Override
											public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
												Toast.makeText(getBaseContext(), "上傳成功加入共享", Toast.LENGTH_SHORT).show();
											}
										});

								}else{
									Toast.makeText(getBaseContext(), "有錯誤或是網路問題無法上傳", Toast.LENGTH_SHORT).show();
									//StorageReference mountainsRef = mStorageRef.child("images/m_3.jpg");
							}
								PDFViewAct.super.onBackPressed();
							}
						}).setNegativeButton("No", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								PDFViewAct.super.onBackPressed();
							}
						}).show();
			} else
				super.onBackPressed();
		}
	}


	@SuppressLint("InlinedApi")
	@Override
	protected void onDestroy() {
		if (m_doc != null) {
			m_view.PDFClose();
			m_doc.Close();
			m_doc = null;
		}
		if (m_asset_stream != null) {
			m_asset_stream.close();
			m_asset_stream = null;
		}
		if (m_http_stream != null) {
			m_http_stream.close();
			m_http_stream = null;
		}
		super.onDestroy();
	}

	@Override
	public void OnPDFPageModified(int pageno) {
		m_modified = true;
	}

	@Override
	public void OnPDFPageChanged(int pageno) {
		if (m_controller != null)
			m_controller.OnPageChanged(pageno);
	}

	@Override
	public void OnPDFAnnotTapped(VPage vpage, Annotation annot) {
		if (m_controller != null)
			m_controller.OnAnnotTapped(annot);
	}

	@Override
	public void OnPDFBlankTapped() {
		if (m_controller != null)
			m_controller.OnBlankTapped();
	}

	//TODO 選取後會跳出選單，但因為加密過所以不能編輯
	@Override
	public void OnPDFSelectEnd(String text) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(com.radaee.viewlib.R.layout.dlg_text, null);
		final RadioGroup rad_group = (RadioGroup) layout.findViewById(com.radaee.viewlib.R.id.rad_group);
		final String sel_text = text;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@SuppressLint("NewApi")
			public void onClick(DialogInterface dialog, int which) {
				if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_copy) {
					ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
					clipboard.setPrimaryClip(ClipData.newPlainText(null, sel_text));
					Toast.makeText(PDFViewAct.this, "複製文本:" + sel_text, Toast.LENGTH_SHORT).show();
				} else if (m_doc.CanSave()) {
					boolean ret = false;
					if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_copy) {
						Toast.makeText(PDFViewAct.this, "離開:" + sel_text, Toast.LENGTH_SHORT).show();
						android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(
								CLIPBOARD_SERVICE);
						android.content.ClipData clip = android.content.ClipData.newPlainText("Radaee", sel_text);
						clipboard.setPrimaryClip(clip);
					} 
					
					else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.Tral_copy) {
						UploadStudentsRecordData("翻譯");
						ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
						clipboard.setPrimaryClip(ClipData.newPlainText(null, sel_text));      	
							//Toast.makeText(PDFViewAct.this, "翻譯文本:" + sel_text, Toast.LENGTH_SHORT).show();
							Intent checkIntent = new Intent();  
					        checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);  
					        startActivityForResult(checkIntent, REQ_TTS_STATUS_CHECK);

					}

					
					else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_highlight)
					{
						ret = m_view.PDFSetSelMarkup(0);
						UploadStudentsRecordData("記號線");
					}

					else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_underline)
					{
						ret = m_view.PDFSetSelMarkup(1);
						UploadStudentsRecordData("下底線");
					}
					else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_strikeout)
					{
						ret = m_view.PDFSetSelMarkup(2);
						UploadStudentsRecordData("刪除線");
					}

					else if (rad_group.getCheckedRadioButtonId() == com.radaee.viewlib.R.id.rad_squiggly)
					{
						ret = m_view.PDFSetSelMarkup(4);
						UploadStudentsRecordData("波浪線");
					}
					//if (!ret)
						//Toast.makeText(PDFViewAct.this, "     ", Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(PDFViewAct.this, "不能寫入或加密！", Toast.LENGTH_SHORT).show();
				dialog.dismiss();
				if (m_controller != null)
					m_controller.OnSelectEnd();
			}
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setTitle("註記");
		builder.setCancelable(false);
		builder.setView(layout);
		AlertDialog dlg = builder.create();
		dlg.show();
	}

	//上傳學生資料
	private void UploadStudentsRecordData(String data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new java.util.Date());
		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("學生資料").child("學生"+MyBroadcast.StudentsName+"號").child("註記").child("註記").child(date).child(data);
		databaseReference.push().setValue(1);
	}

	@Override
	public void OnPDFOpenURI(String uri) {
		try {
			Intent intent = new Intent();
			intent.setAction("android.intent.action.VIEW");
			Uri content_url = Uri.parse(uri);
			intent.setData(content_url);
			startActivity(intent);
		} catch (Exception e) {
			Toast.makeText(PDFViewAct.this, "打開網址:" + uri, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void OnPDFOpenJS(String js) {
		Toast.makeText(PDFViewAct.this, "執行javascript", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnPDFOpenMovie(String path) {
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));

		startActivity(intent);
		Toast.makeText(PDFViewAct.this, "todo: play movie", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnPDFOpenSound(int[] paras, String path) {
		Toast.makeText(PDFViewAct.this, "todo: play sound", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnPDFOpenAttachment(String path) {
		Toast.makeText(PDFViewAct.this, "todo: treat attachment", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void OnPDFOpen3D(String path) {
		Toast.makeText(PDFViewAct.this, "todo: play 3D module", Toast.LENGTH_SHORT).show();
	}
}
