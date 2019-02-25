package com.radaee.reader;


import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.InputType;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.radaee.pdf.Document;
import com.radaee.pdf.Ink;
import com.radaee.pdf.Matrix;
import com.radaee.pdf.Page;
import com.radaee.util.ComboList;
import com.radaee.util.PictureUtil;
import com.radaee.Broadcast.MyBroadcast;
import com.radaee.Model.StudentsNote;
import com.radaee.pdf.Global;

import com.radaee.pdf.Page.Annotation;
import com.radaee.view.PDFLayout;
import com.radaee.view.PDFLayout.LayoutListener;
import com.radaee.view.PDFLayout.PDFPos;
import com.radaee.view.PDFLayoutDual;
import com.radaee.view.PDFLayoutVert;
import com.radaee.view.VPage;
import com.radaee.view.VSel;
import com.radaee.viewlib.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;

//import org.geometerplus.zlibrary.text.view.SaveValue;

public class PDFLayoutView extends  View implements LayoutListener, OnItemClickListener, PopupWindow.OnDismissListener, onImageViewCommand, ShowImageToNote {
	static final protected int STA_NONE = 0;
	static final protected int STA_ZOOM = 1;
	static final protected int STA_SELECT = 2;
	static final protected int STA_INK = 3;
	static final protected int STA_RECT = 4;
	static final protected int STA_ELLIPSE = 5;
	static final protected int STA_NOTE = 6;
	static final protected int STA_LINE = 7;
	static final protected int STA_STAMP = 8;
	static final protected int STA_ANNOT = 100;
	public static String WordNum;	//得知在哪個學生目錄下
	static protected String WordNum2;
	static public boolean WordNum_btn; //得知共享是否開啟
	static protected String WordworkNum; //得知在第幾課下
	static protected String View_mode="直向";//得知查看方式
	static protected String pageNumber;//得知在第幾頁
	private Bitmap.Config m_bmp_format = Bitmap.Config.ALPHA_8;
	private PDFLayout m_layout;
	private Document m_doc;
	private int m_status = STA_NONE;
	private boolean m_zooming = false;
	private int m_pageno = 0;
	private boolean work_share=false;
	private PDFPos m_goto_pos = null;
	private MediaRecorder mediaRecorder = null;
	private boolean arm_work=true;
	private GestureDetector m_gesture = null;
	private Annotation m_annot = null;
	private PDFPos m_annot_pos = null;
	private VPage m_annot_page = null;
	private float m_annot_rect[];
	private float m_annot_rect0[];
	private float m_annot_x0;
	private float m_annot_y0;
    private String Link;
    private PDFViewController m_pdfvc;
	private Ink m_ink = null;
	private Bitmap m_icon = null;
	private float m_rects[];
	private VPage m_note_pages[];
	private int m_note_indecs[];
	private PDFLayoutListener m_listener;
	private VSel m_sel = null;
	private int m_edit_type = 0;
	private int m_combo_item = -1;
	private boolean m_rtol = false;
	private boolean amrPlayBtn = true;
	private PopupWindow m_pEdit = null;
	private PopupWindow m_pCombo = null;
	private Bitmap m_sel_icon1 = null;
	private Bitmap m_sel_icon2 = null;
	private MediaPlayer player;
	private File audioFile;
	private File audioFilePH;
	private FileOutputStream fos;
	/*
	 * // 宣告拍照介面元件SurfaceView private SurfaceView surfaceView1; //
	 * 宣告介面控制元件SurfaceHolder private SurfaceHolder surfaceHolder; // 宣告照相機
	 * private Camera camera;
	 */
	private StorageReference mStorageRef;
	private UploadTask uploadTask;
	android.hardware.Camera camera;
	private DatabaseReference mDatabase;
	private static String fileName = "record.amr";
	private static String fileNamePh = "record.jpg";
	private static ImageView IV;
	private String ST_ID=WordNum+"_";
	private PDFViewAct activity;
	String[] proj = {MediaStore.Images.Media.DATA};
	private String IVP;
	private ImageView btn_annot_note;
	public static final  int ImageNote = 101;


	RelativeLayout layout;
	private static EditText subj;
	private static EditText content;
	private static Button voicebtn;
	private static Button photobtn;
	private static Button savephbutton;
	private static Button vstbtn;
	private static TextView tv;
	private static Button playbtn;

	FirebaseStorage storage;
	StorageReference storageReference;
	DatabaseReference DatabaseRef;
	StorageReference phref;

	//TODO 回呼是從PDFViewAct 讀到PDF的URI 回傳回來的，為了顯示在此頁面上
	public void onShowImage(String path) {
		
		IV.setImageBitmap(PictureUtil.getSmallBitmap(path, IV.getWidth(), IV.getHeight()));
		IVP=path;
		File SDCardpath = Environment.getExternalStorageDirectory();
		File myDataPath = new File(SDCardpath.getAbsolutePath() + "/" +WordNum);
		if (!myDataPath.exists())
			myDataPath.mkdirs();
		audioFilePH = new File(SDCardpath.getAbsolutePath() + "/" +WordNum+"/" + fileNamePh);
		File save_image = new File(myDataPath,fileNamePh);
		IV.setImageBitmap(PictureUtil.getSmallBitmap(IVP, IV.getWidth(), IV.getHeight()));
		IV.setDrawingCacheEnabled(true);
		Bitmap bmp = IV.getDrawingCache();
		try {
			save_image.createNewFile();
			fos = new FileOutputStream(save_image);
			 bmp.compress(Bitmap.CompressFormat.JPEG,100,fos);
             fos.flush();
             fos.close();
		} catch (FileNotFoundException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動產生的 catch 區塊
			e.printStackTrace();
		}
	}


	//TODO 是PDFView 一開始在OnCreate的時候呼叫PDFLayoutView的setActivity方法，並把自己丟進去，讓PDFLayoutView去叫PDFView裡面的註冊回呼方法，這樣PDFView得到Uri時候回乎給PDFLayoutView去載入PDF
	public void setActivity(PDFViewAct _activity) {
		activity = _activity;
		activity.setOnCommandListenter(this, this);
	}

	public void ShowImageToNote(Bitmap pbmp)
	{
		IV = (ImageView) layout.findViewById(R.id.imageView);
		IV.setImageBitmap(pbmp);
	}

	private class DownloadImageTask extends AsyncTask<String, Integer, Bitmap> {
		protected Bitmap doInBackground(String... strings){
			try{
				URL url = new URL(strings[0]);
				HttpURLConnection connection = (HttpURLConnection)url.openConnection();
				connection.setDoInput(true);
				connection.connect();
				InputStream input = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				return bitmap;
			}catch (IOException e){
				e.printStackTrace();
				return null;
			}
		}
		protected void onPostExecute(Bitmap bitmap){
			IV.setImageBitmap(bitmap);
		}
	}

	class PDFGestureListener extends GestureDetector.SimpleOnGestureListener {// ***手勢區(長按.2下)***
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (m_status == STA_NONE) {
				float dx = e2.getX() - e1.getX();
				float dy = e2.getY() - e1.getY();
				return m_layout.vFling(m_hold_docx, m_hold_docy, dx, dy, velocityX, velocityY);
			} else
				return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return false;
		}

		@Override
		public boolean onDown(MotionEvent e) {
			return false;
		}

		@Override
		public void onLongPress(MotionEvent e) {
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}

		@Override
		public void onShowPress(MotionEvent e) {
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			return false;
		}


		boolean Touch = true;
		@Override
		public boolean onSingleTapUp(MotionEvent e) {// ***按一下***
			if (m_status == STA_NONE || m_status == STA_ANNOT) {
				m_annot_pos = m_layout.vGetPos((int) e.getX(), (int) e.getY());
				m_annot_page = m_layout.vGetPage(m_annot_pos.pageno);
				Page page = m_doc.GetPage(m_annot_page.GetPageNo());
				if (page == null)
					m_annot = null;
				else
					m_annot = page.GetAnnotFromPoint(m_annot_pos.x, m_annot_pos.y);
				if (m_annot == null) {
					m_annot_page = null;
					m_annot_pos = null;
					m_annot_rect = null;
					if (m_listener != null) {
						if (m_status == STA_ANNOT)
							m_listener.OnPDFAnnotTapped(m_annot_page, null);
						else
							m_listener.OnPDFBlankTapped();
					}
					m_status = STA_NONE;

				} else {
					page.ObjsStart();
					m_annot_rect = m_annot.GetRect();
					float tmp = m_annot_rect[1];
					m_annot_rect[0] = m_annot_page.GetVX(m_annot_rect[0]) - m_layout.vGetX();
					m_annot_rect[1] = m_annot_page.GetVY(m_annot_rect[3]) - m_layout.vGetY();
					m_annot_rect[2] = m_annot_page.GetVX(m_annot_rect[2]) - m_layout.vGetX();
					m_annot_rect[3] = m_annot_page.GetVY(tmp) - m_layout.vGetY();
					m_status = STA_ANNOT;
					if (m_doc.CanSave() && m_annot.GetEditType() > 0) {
						int[] location = new int[2];
						getLocationOnScreen(location);
						m_pEdit.setWidth((int) (m_annot_rect[2] - m_annot_rect[0]));
						m_pEdit.setHeight((int) (m_annot_rect[3] - m_annot_rect[1]));
						EditText edit = (EditText) m_pEdit.getContentView().findViewById(com.radaee.viewlib.R.id.annot_text);
						edit.setBackgroundColor(0xFFFFFFC0);
						float fsize = m_annot.GetEditTextSize() * m_layout.vGetScale();
						edit.setTextSize(TypedValue.COMPLEX_UNIT_PX, fsize);
						edit.setPadding(2, 2, 2, 2);
						switch (m_annot.GetEditType()) {
						case 1:
							edit.setSingleLine();
							edit.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_NORMAL);
							break;
						case 2:
							edit.setSingleLine();
							edit.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_PASSWORD);
							break;
						case 3:
							edit.setSingleLine(false);
							edit.setInputType(InputType.TYPE_CLASS_TEXT + InputType.TYPE_TEXT_VARIATION_NORMAL);
							break;
						}
						int maxlen = m_annot.GetEditMaxlen();
						if (maxlen > 0)
							edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxlen) });
						else
							edit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(1020) });
						edit.setText(m_annot.GetEditText());
						m_edit_type = 1;
						m_pEdit.showAtLocation(PDFLayoutView.this, Gravity.NO_GRAVITY,
								(int) m_annot_rect[0] + location[0], (int) m_annot_rect[1] + location[1]);
					} else if (m_doc.CanSave() && m_annot.GetComboItemCount() >= 0) {
						int[] location = new int[2];
						getLocationOnScreen(location);
						String opts[] = new String[m_annot.GetComboItemCount()];
						int cur = 0;
						while (cur < opts.length) {
							opts[cur] = m_annot.GetComboItem(cur);
							cur++;
						}
						m_pCombo.setWidth((int) (m_annot_rect[2] - m_annot_rect[0]));
						if ((m_annot_rect[3] - m_annot_rect[1] - 4) * opts.length > 250)
							m_pCombo.setHeight(250);
						else
							m_pCombo.setHeight((int) (m_annot_rect[3] - m_annot_rect[1] - 4) * opts.length);
						ComboList combo = (ComboList) m_pCombo.getContentView().findViewById(com.radaee.viewlib.R.id.annot_combo);
						combo.set_opts(opts);
						combo.setOnItemClickListener(PDFLayoutView.this);
						m_edit_type = 2;
						m_combo_item = -1;
						m_pCombo.showAtLocation(PDFLayoutView.this, Gravity.NO_GRAVITY,
								(int) m_annot_rect[0] + location[0], (int) (m_annot_rect[3] + location[1]));
					} else if (m_listener != null)
						m_listener.OnPDFAnnotTapped(m_annot_page, m_annot);
					invalidate();
				}
				return true;
			}
			return false;
		}
	}

	public interface PDFLayoutListener {// **布局監聽器**
		public void OnPDFPageModified(int pageno);

		public void OnPDFPageChanged(int pageno);

		public void OnPDFAnnotTapped(VPage vpage, Annotation annot);

		public void OnPDFBlankTapped();

		public void OnPDFSelectEnd(String text);

		public void OnPDFOpenURI(String uri);

		public void OnPDFOpenJS(String js);

		public void OnPDFOpenMovie(String path);

		public void OnPDFOpenSound(int[] paras, String path);

		public void OnPDFOpenAttachment(String path);

		public void OnPDFOpen3D(String path);
	}

	class PDFVPageSet {// ***頁面***
		PDFVPageSet(int max_len) {
			pages = new VPage[max_len];
			pages_cnt = 0;
		}

		void Insert(VPage vpage) {
			int cur = 0;
			for (cur = 0; cur < pages_cnt; cur++) {
				if (pages[cur] == vpage)
					return;
			}
			pages[cur] = vpage;
			pages_cnt++;
		}

		VPage pages[];
		int pages_cnt;
	}

	public PDFLayoutView(Context context) {
		super(context);
		m_pEdit = new PopupWindow(LayoutInflater.from(context).inflate(com.radaee.viewlib.R.layout.pop_edit, null));
		m_pCombo = new PopupWindow(LayoutInflater.from(context).inflate(com.radaee.viewlib.R.layout.pop_combo, null));
		Drawable dw = new ColorDrawable(0);
		m_pEdit.setOnDismissListener(this);
		m_pCombo.setOnDismissListener(this);
		m_pEdit.setFocusable(true);
		m_pEdit.setTouchable(true);
		m_pEdit.setBackgroundDrawable(dw);
		m_pCombo.setFocusable(true);
		m_pCombo.setTouchable(true);
		m_pCombo.setBackgroundDrawable(dw);
		m_doc = null;
		m_gesture = new GestureDetector(context, new PDFGestureListener());
	}

	public PDFLayoutView(Context context, AttributeSet attrs) {
		super(context, attrs);
		m_pEdit = new PopupWindow(LayoutInflater.from(context).inflate(com.radaee.viewlib.R.layout.pop_edit, null));
		m_pCombo = new PopupWindow(LayoutInflater.from(context).inflate(com.radaee.viewlib.R.layout.pop_combo, null));
		Drawable dw = new ColorDrawable(0);
		m_pEdit.setOnDismissListener(this);
		m_pCombo.setOnDismissListener(this);
		m_pEdit.setFocusable(true);
		m_pEdit.setTouchable(true);
		m_pEdit.setBackgroundDrawable(dw);
		m_pCombo.setFocusable(true);
		m_pCombo.setTouchable(true);
		m_pCombo.setBackgroundDrawable(dw);
		m_doc = null;
		m_gesture = new GestureDetector(context, new PDFGestureListener());
		btn_annot_note = (ImageView)findViewById(com.radaee.viewlib.R.id.btn_annot_note);

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		if (m_layout != null && m_status != STA_ANNOT && w > 0 && h > 0) {
			m_layout.vResize(w, h);
			if (m_goto_pos != null) {
				m_layout.vSetPos(0, 0, m_goto_pos);
				m_goto_pos = null;
				invalidate();
			}
		}
	}

	private void onDrawSelect(Canvas canvas) {
		if (m_status == STA_SELECT && m_sel != null && m_annot_page != null) {
			int orgx = m_annot_page.GetVX(0) - m_layout.vGetX();
			int orgy = m_annot_page.GetVY(m_doc.GetPageHeight(m_annot_page.GetPageNo())) - m_layout.vGetY();
			float scale = m_layout.vGetScale();
			float pheight = m_doc.GetPageHeight(m_annot_page.GetPageNo());
			m_sel.DrawSel(canvas, scale, pheight, orgx, orgy);
			int rect1[] = m_sel.GetRect1(scale, pheight, orgx, orgy);
			int rect2[] = m_sel.GetRect2(scale, pheight, orgx, orgy);
			if (rect1 != null && rect2 != null) {
				canvas.drawBitmap(m_sel_icon1, rect1[0] - m_sel_icon1.getWidth(), rect1[1] - m_sel_icon1.getHeight(),
						null);
				canvas.drawBitmap(m_sel_icon2, rect2[2], rect2[3], null);
			}
		}
	}

	private void onDrawAnnot(Canvas canvas) {
		if (m_status == STA_ANNOT) {
			Paint paint = new Paint();
			paint.setStyle(Style.STROKE);
			paint.setStrokeWidth(2);
			paint.setARGB(0x80, 0, 0, 0);
			canvas.drawRect(m_annot_rect[0], m_annot_rect[1], m_annot_rect[2], m_annot_rect[3], paint);
		}
	}

	private void onDrawRect(Canvas canvas) {
		if (m_status == STA_RECT && m_rects != null) {
			int len = m_rects.length;
			int cur;
			Paint paint1 = new Paint();
			Paint paint2 = new Paint();
			paint1.setStyle(Style.STROKE);
			paint1.setStrokeWidth(3);
			paint1.setARGB(0x80, 0xFF, 0, 0);
			paint2.setStyle(Style.FILL);
			paint2.setARGB(0x80, 0, 0, 0xFF);
			for (cur = 0; cur < len; cur += 4) {
				float rect[] = new float[4];
				if (m_rects[cur] > m_rects[cur + 2]) {
					rect[0] = m_rects[cur + 2];
					rect[2] = m_rects[cur];
				} else {
					rect[0] = m_rects[cur];
					rect[2] = m_rects[cur + 2];
				}
				if (m_rects[cur + 1] > m_rects[cur + 3]) {
					rect[1] = m_rects[cur + 3];
					rect[3] = m_rects[cur + 1];
				} else {
					rect[1] = m_rects[cur + 1];
					rect[3] = m_rects[cur + 3];
				}
				canvas.drawRect(rect[0], rect[1], rect[2], rect[3], paint1);
				canvas.drawRect(rect[0] + 1.5f, rect[1] + 1.5f, rect[2] - 1.5f, rect[3] - 1.5f, paint2);
			}
		}
	}

	private void onDrawLine(Canvas canvas) {
		if (m_status == STA_LINE && m_rects != null) {
			int len = m_rects.length;
			int cur;
			Paint paint1 = new Paint();
			paint1.setStyle(Style.STROKE);
			paint1.setStrokeWidth(3);
			paint1.setARGB(0x80, 0xFF, 0, 0);
			for (cur = 0; cur < len; cur += 4) {
				canvas.drawLine(m_rects[cur], m_rects[cur + 1], m_rects[cur + 2], m_rects[cur + 3], paint1);
			}
		}
	}

	private void onDrawStamp(Canvas canvas) {
		if (m_status == STA_STAMP && m_rects != null) {
			int len = m_rects.length;
			int cur;
			for (cur = 0; cur < len; cur += 4) {
				float rect[] = new float[4];
				if (m_rects[cur] > m_rects[cur + 2]) {
					rect[0] = m_rects[cur + 2];
					rect[2] = m_rects[cur];
				} else {
					rect[0] = m_rects[cur];
					rect[2] = m_rects[cur + 2];
				}
				if (m_rects[cur + 1] > m_rects[cur + 3]) {
					rect[1] = m_rects[cur + 3];
					rect[3] = m_rects[cur + 1];
				} else {
					rect[1] = m_rects[cur + 1];
					rect[3] = m_rects[cur + 3];
				}
				if (m_icon != null) {
					Rect rc = new Rect();
					rc.left = (int) rect[0];
					rc.top = (int) rect[1];
					rc.right = (int) rect[2];
					rc.bottom = (int) rect[3];
					canvas.drawBitmap(m_icon, null, rc, null);
				}
			}
		}
	}

	private void onDrawEllipse(Canvas canvas) {
		if (m_status == STA_ELLIPSE && m_rects != null) {
			int len = m_rects.length;
			int cur;
			Paint paint1 = new Paint();
			Paint paint2 = new Paint();
			paint1.setStyle(Style.STROKE);
			paint1.setStrokeWidth(3);
			paint1.setARGB(0x80, 0xFF, 0, 0);
			paint2.setStyle(Style.FILL);
			paint2.setARGB(0x80, 0, 0, 0xFF);
			for (cur = 0; cur < len; cur += 4) {
				float rect[] = new float[4];
				if (m_rects[cur] > m_rects[cur + 2]) {
					rect[0] = m_rects[cur + 2];
					rect[2] = m_rects[cur];
				} else {
					rect[0] = m_rects[cur];
					rect[2] = m_rects[cur + 2];
				}
				if (m_rects[cur + 1] > m_rects[cur + 3]) {
					rect[1] = m_rects[cur + 3];
					rect[3] = m_rects[cur + 1];
				} else {
					rect[1] = m_rects[cur + 1];
					rect[3] = m_rects[cur + 3];
				}
				RectF rc = new RectF();
				rc.left = rect[0];
				rc.top = rect[1];
				rc.right = rect[2];
				rc.bottom = rect[3];
				canvas.drawOval(rc, paint1);
				rc.left += 1.5f;
				rc.top += 1.5f;
				rc.right -= 1.5f;
				rc.bottom -= 1.5f;
				canvas.drawOval(rc, paint2);
			}
		}
	}

	static private Paint m_info_paint = new Paint();

	@Override
	protected void onDraw(Canvas canvas) {// ***畫形狀***
		if (m_layout != null) {
			m_layout.vDraw(canvas, m_zooming || m_status == STA_ZOOM);
			onDrawSelect(canvas);
			onDrawRect(canvas);
			onDrawEllipse(canvas);
			onDrawAnnot(canvas);
			onDrawLine(canvas);
			onDrawStamp(canvas);
			if (m_status == STA_INK && m_ink != null) {
				m_ink.OnDraw(canvas, 0, 0);
			}
		}
		ActivityManager mgr = (ActivityManager) getContext().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
		mgr.getMemoryInfo(info);
		m_info_paint.setARGB(255, 255, 0, 0);
		m_info_paint.setTextSize(30);
		canvas.drawText("AvialMem:" + info.availMem / (1024 * 1024) + " M", 20, 150, m_info_paint);
	}

	private float m_hold_x;
	private float m_hold_y;
	private int m_hold_docx;
	private int m_hold_docy;
	private PDFPos m_zoom_pos;
	private float m_zoom_dis0;
	private float m_zoom_scale;

	private boolean onTouchNone(MotionEvent event) {// ***畫完不觸摸時***
		if (m_status != STA_NONE)
			return false;
		if (m_gesture.onTouchEvent(event))
			return true;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			m_hold_x = event.getX();
			m_hold_y = event.getY();
			m_hold_docx = m_layout.vGetX();
			m_hold_docy = m_layout.vGetY();
			m_layout.vScrollAbort();
			invalidate();
			break;
		case MotionEvent.ACTION_MOVE:
			if (m_hold_x <= -10000 && m_hold_y <= -10000) {
				m_hold_x = event.getX();
				m_hold_y = event.getY();
				m_hold_docx = m_layout.vGetX();
				m_hold_docy = m_layout.vGetY();
			} else {
				m_layout.vSetX((int) (m_hold_docx + m_hold_x - event.getX()));
				m_layout.vSetY((int) (m_hold_docy + m_hold_y - event.getY()));
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (m_hold_x <= -10000 && m_hold_y <= -10000) {
				m_hold_x = event.getX();
				m_hold_y = event.getY();
				m_hold_docx = m_layout.vGetX();
				m_hold_docy = m_layout.vGetY();
			} else {
				m_layout.vSetX((int) (m_hold_docx + m_hold_x - event.getX()));
				m_layout.vSetY((int) (m_hold_docy + m_hold_y - event.getY()));
				invalidate();
				m_layout.vMoveEnd();
			}
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			if (event.getPointerCount() >= 2) {
				m_status = STA_ZOOM;
				m_hold_x = (event.getX(0) + event.getX(1)) / 2;
				m_hold_y = (event.getY(0) + event.getY(1)) / 2;
				m_zoom_pos = m_layout.vGetPos((int) m_hold_x, (int) m_hold_y);
				float dx = event.getX(0) - event.getX(1);
				float dy = event.getY(0) - event.getY(1);
				m_zoom_dis0 = (float)Math.sqrt(dx * dx + dy * dy);
				m_zoom_scale = m_layout.vGetZoom();
				m_status = STA_ZOOM;
				m_layout.vZoomStart();
			}
			break;
		}
		return true;
	}

	private boolean onTouchZoom(MotionEvent event) {// ***觸摸縮放***
		if (m_status != STA_ZOOM)
			return false;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_MOVE:
			if (m_status == STA_ZOOM) {
				float dx = event.getX(0) - event.getX(1);
				float dy = event.getY(0) - event.getY(1);
				float dis1 = (float)Math.sqrt(dx * dx + dy * dy);
				m_layout.vZoomSet((int) m_hold_x, (int) m_hold_y, m_zoom_pos, m_zoom_scale * dis1 / m_zoom_dis0);
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
		case MotionEvent.ACTION_CANCEL:
			if (m_status == STA_ZOOM && event.getPointerCount() <= 2) {
				float dx = event.getX(0) - event.getX(1);
				float dy = event.getY(0) - event.getY(1);
				float dis1 = (float)Math.sqrt(dx * dx + dy * dy);
				m_layout.vZoomSet((int) m_hold_x, (int) m_hold_y, m_zoom_pos, m_zoom_scale * dis1 / m_zoom_dis0);
				m_hold_x = -10000;
				m_hold_y = -10000;
				m_status = STA_NONE;
				m_zooming = true;
				m_layout.vZoomConfirmed();
				invalidate();
			}
			break;
		}
		return true;
	}

	private boolean onTouchSelect(MotionEvent event) {
		if (m_status != STA_SELECT)
			return false;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			m_hold_x = event.getX();
			m_hold_y = event.getY();
			if (m_sel != null) {
				m_sel.Clear();
				m_sel = null;
			}
			m_annot_pos = m_layout.vGetPos((int) m_hold_x, (int) m_hold_y);
			m_annot_page = m_layout.vGetPage(m_annot_pos.pageno);
			m_sel = new VSel(m_doc.GetPage(m_annot_pos.pageno));
			break;
		case MotionEvent.ACTION_MOVE:
			if (m_sel != null) {
				m_sel.SetSel(m_annot_pos.x, m_annot_pos.y, m_annot_page.ToPDFX(event.getX(), m_layout.vGetX()),
						m_annot_page.ToPDFY(event.getY(), m_layout.vGetY()));
				invalidate();
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			if (m_sel != null) {
				m_sel.SetSel(m_annot_pos.x, m_annot_pos.y, m_annot_page.ToPDFX(event.getX(), m_layout.vGetX()),
						m_annot_page.ToPDFY(event.getY(), m_layout.vGetY()));
				invalidate();
				if (m_listener != null)
					m_listener.OnPDFSelectEnd(m_sel.GetSelString());
			}
			break;
		}
		return true;
	}

	private boolean onTouchInk(MotionEvent event) {
		if (m_status != STA_INK)
			return false;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			if (m_annot_page == null) {
				PDFPos pos = m_layout.vGetPos((int) event.getX(), (int) event.getY());
				m_annot_page = m_layout.vGetPage(pos.pageno);
			}
			m_ink.OnDown(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			m_ink.OnMove(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_UP:
			PDFSetInk(1);
			break;
		case MotionEvent.ACTION_CANCEL:
			m_ink.OnUp(event.getX(), event.getY());
			break;
		}
		invalidate();
		return true;
	}

	private boolean onTouchRect(MotionEvent event) {
		if (m_status != STA_RECT)
			return false;
		int len = 0;
		if (m_rects != null)
			len = m_rects.length;
		int cur = 0;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			float rects[] = new float[len + 4];
			for (cur = 0; cur < len; cur++)
				rects[cur] = m_rects[cur];
			len += 4;
			rects[cur + 0] = event.getX();
			rects[cur + 1] = event.getY();
			rects[cur + 2] = event.getX();
			rects[cur + 3] = event.getY();
			m_rects = rects;
			break;
		case MotionEvent.ACTION_MOVE:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			UploadStudentsRecordData("矩形記號");
			PDFSetRect(1);
			break;
		case MotionEvent.ACTION_CANCEL:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		}
		invalidate();
		return true;
	}

	private boolean onTouchEllipse(MotionEvent event) {
		if (m_status != STA_ELLIPSE)
			return false;
		int len = 0;
		if (m_rects != null)
			len = m_rects.length;
		int cur = 0;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			float rects[] = new float[len + 4];
			for (cur = 0; cur < len; cur++)
				rects[cur] = m_rects[cur];
			len += 4;
			rects[cur + 0] = event.getX();
			rects[cur + 1] = event.getY();
			rects[cur + 2] = event.getX();
			rects[cur + 3] = event.getY();
			m_rects = rects;
			break;
		case MotionEvent.ACTION_MOVE:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			UploadStudentsRecordData("圓形記號");
			PDFSetEllipse(1);
			break;
		case MotionEvent.ACTION_CANCEL:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		}
		invalidate();
		return true;
	}

	private boolean onTouchAnnot(MotionEvent event) {
		if (m_status != STA_ANNOT)
			return false;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			m_annot_x0 = event.getX();
			m_annot_y0 = event.getY();
			if (m_annot_x0 > m_annot_rect[0] && m_annot_y0 > m_annot_rect[1] && m_annot_x0 < m_annot_rect[2]
					&& m_annot_y0 < m_annot_rect[3]) {
				m_annot_rect0 = new float[4];
				m_annot_rect0[0] = m_annot_rect[0];
				m_annot_rect0[1] = m_annot_rect[1];
				m_annot_rect0[2] = m_annot_rect[2];
				m_annot_rect0[3] = m_annot_rect[3];
			} else
				m_annot_rect0 = null;
			break;
		case MotionEvent.ACTION_MOVE:
			if (m_annot_rect0 != null) {
				float x = event.getX();
				float y = event.getY();
				m_annot_rect[0] = m_annot_rect0[0] + x - m_annot_x0;
				m_annot_rect[1] = m_annot_rect0[1] + y - m_annot_y0;
				m_annot_rect[2] = m_annot_rect0[2] + x - m_annot_x0;
				m_annot_rect[3] = m_annot_rect0[3] + y - m_annot_y0;
			}
			break;
		case MotionEvent.ACTION_UP:
			PDFEditAnnot();
			break;
		case MotionEvent.ACTION_CANCEL:
			if (m_annot_rect0 != null) {
				float x = event.getX();
				float y = event.getY();
				PDFPos pos = m_layout.vGetPos((int) x, (int) y);
				m_annot_rect[0] = m_annot_rect0[0] + x - m_annot_x0;
				m_annot_rect[1] = m_annot_rect0[1] + y - m_annot_y0;
				m_annot_rect[2] = m_annot_rect0[2] + x - m_annot_x0;
				m_annot_rect[3] = m_annot_rect0[3] + y - m_annot_y0;
				if (m_annot_page.GetPageNo() == pos.pageno) {
					m_annot_rect0[0] = m_annot_page.ToPDFX(m_annot_rect[0], m_layout.vGetX());
					m_annot_rect0[1] = m_annot_page.ToPDFY(m_annot_rect[3], m_layout.vGetY());
					m_annot_rect0[2] = m_annot_page.ToPDFX(m_annot_rect[2], m_layout.vGetX());
					m_annot_rect0[3] = m_annot_page.ToPDFY(m_annot_rect[1], m_layout.vGetY());
					m_annot.SetRect(m_annot_rect0[0], m_annot_rect0[1], m_annot_rect0[2], m_annot_rect0[3]);
					m_layout.vRenderSync(m_annot_page);
					if (m_listener != null)
						m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
				} else {
					VPage vpage = m_layout.vGetPage(pos.pageno);
					Page page = m_doc.GetPage(vpage.GetPageNo());
					if (page != null) {
						page.ObjsStart();
						m_annot_rect0[0] = vpage.ToPDFX(m_annot_rect[0], m_layout.vGetX());
						m_annot_rect0[1] = vpage.ToPDFY(m_annot_rect[3], m_layout.vGetY());
						m_annot_rect0[2] = vpage.ToPDFX(m_annot_rect[2], m_layout.vGetX());
						m_annot_rect0[3] = vpage.ToPDFY(m_annot_rect[1], m_layout.vGetY());
						m_annot.MoveToPage(page, m_annot_rect0);
						// page.CopyAnnot(m_annot, m_annot_rect0);
						page.Close();
					}
					m_layout.vRenderSync(m_annot_page);
					m_layout.vRenderSync(vpage);
					if (m_listener != null) {
						m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
						m_listener.OnPDFPageModified(vpage.GetPageNo());
					}
				}
			}
			PDFEndAnnot();
			break;
		}
		invalidate();
		return true;
	}

	private boolean onTouchLine(MotionEvent event) {
		if (m_status != STA_LINE)
			return false;
		int len = 0;
		if (m_rects != null)
			len = m_rects.length;
		int cur = 0;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			float rects[] = new float[len + 4];
			for (cur = 0; cur < len; cur++)
				rects[cur] = m_rects[cur];
			len += 4;
			rects[cur + 0] = event.getX();
			rects[cur + 1] = event.getY();
			rects[cur + 2] = event.getX();
			rects[cur + 3] = event.getY();
			m_rects = rects;
			break;
		case MotionEvent.ACTION_MOVE:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			UploadStudentsRecordData("線型記號");
			PDFSetLine(1);
			break;
		case MotionEvent.ACTION_CANCEL:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		}
		invalidate();
		return true;
	}

	private boolean onTouchStamp(MotionEvent event) {
		if (m_status != STA_STAMP)
			return false;
		int len = 0;
		if (m_rects != null)
			len = m_rects.length;
		int cur = 0;
		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			float rects[] = new float[len + 4];
			for (cur = 0; cur < len; cur++)
				rects[cur] = m_rects[cur];
			len += 4;
			rects[cur + 0] = event.getX();
			rects[cur + 1] = event.getY();
			rects[cur + 2] = event.getX();
			rects[cur + 3] = event.getY();
			m_rects = rects;
			break;
		case MotionEvent.ACTION_MOVE:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		case MotionEvent.ACTION_UP:
			UploadStudentsRecordData("星型記號");
			PDFSetStamp(1);
			break;
		case MotionEvent.ACTION_CANCEL:
			m_rects[len - 2] = event.getX();
			m_rects[len - 1] = event.getY();
			break;
		}
		invalidate();
		return true;
	}


	private boolean onTouchNote(MotionEvent event) {
		if (m_status != STA_NOTE)
			return false;

		switch (event.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			PDFPos pos = m_layout.vGetPos((int) event.getX(), (int) event.getY());
			VPage vpage = m_layout.vGetPage(pos.pageno);
			Page page = m_doc.GetPage(vpage.GetPageNo());
			if (page != null) {
				page.ObjsStart();
				if (m_note_pages == null) {
					m_note_pages = new VPage[1];
					m_note_indecs = new int[1];
					m_note_pages[0] = vpage;
					m_note_indecs[0] = page.GetAnnotCount();
				} else {
					int cur = 0;
					int cnt = m_note_pages.length;
					while (cur < cnt) {
						if (m_note_pages[cur] == vpage)
							break;
						cur++;
					}
					if (cur >= cnt)// append 1 page
					{
						VPage pages[] = new VPage[cnt + 1];
						int indecs[] = new int[cnt + 1];
						for (cur = 0; cur < cnt; cur++) {
							pages[cur] = m_note_pages[cur];
							indecs[cur] = m_note_indecs[cur];
						}
						pages[cnt] = vpage;
						indecs[cnt] = page.GetAnnotCount();
						m_note_pages = pages;
						m_note_indecs = indecs;
					}
				}
				float pt[] = new float[2];
				pt[0] = pos.x;
				pt[1] = pos.y;
				page.AddAnnotText(pt);
				m_layout.vRenderSync(vpage);
				invalidate();
				page.Close();

				if (m_listener != null)
					m_listener.OnPDFPageModified(vpage.GetPageNo());
			}
			break;
			case MotionEvent.ACTION_UP:
				PDFSetNote(1);//手指抬起後 關閉註記
               // m_status = STA_ANNOT;
              //  PDFEditAnnot();
				break;
		}
		return true;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (m_layout == null)
			return false;
		if (onTouchNone(event))
			return true;
		if (onTouchZoom(event))
			return true;
		if (onTouchSelect(event))
			return true;
		if (onTouchInk(event))
			return true;
		if (onTouchRect(event))
			return true;
		if (onTouchEllipse(event))
			return true;
		if (onTouchNote(event))
			return true;
		if (onTouchLine(event))
			return true;
		if (onTouchStamp(event))
			return true;
		if (onTouchAnnot(event))
			return true;
		return true;
	}

	@Override
	public void computeScroll() {
		if (m_layout != null && m_layout.vScrollCompute())
			invalidate();
	}

	public void PDFSetView(int style) {
		PDFPos pos = null;
		if (m_layout != null)
			pos = m_layout.vGetPos(0, 0);
		PDFClose();
		switch (style) {
		case 3: {
			PDFLayoutDual layout = new PDFLayoutDual(getContext());
			boolean paras[] = new boolean[m_doc.GetPageCount()];
			int cur = 0;
			while (cur < paras.length) {
				paras[cur] = false;
				cur++;
			}
			layout.vSetLayoutPara(null, paras, m_rtol, true);
			m_layout = layout;
		}
			break;
		case 4:
		case 6: {
			PDFLayoutDual layout = new PDFLayoutDual(getContext());
			layout.vSetLayoutPara(null, null, m_rtol, true);
			m_layout = layout;
		}
			break;
		default: {
			PDFLayoutVert layout = new PDFLayoutVert(getContext());
			m_layout = layout;
		}
			break;
		}
		m_layout.vOpen(m_doc, this);
		if (m_bmp_format != Bitmap.Config.ALPHA_8) {
			m_layout.vSetBmpFormat(m_bmp_format);
			m_bmp_format = Bitmap.Config.ALPHA_8;
		}
		if (getWidth() > 0 && getHeight() > 0) {
			m_layout.vResize(getWidth(), getHeight());
			if (m_goto_pos != null) {
				m_layout.vSetPos(0, 0, m_goto_pos);
				m_goto_pos = null;
				invalidate();
			} else if (pos != null) {
				m_layout.vSetPos(0, 0, pos);
				m_layout.vMoveEnd();
			}
		}
		invalidate();
	}

	public void PDFOpen(Document doc, PDFLayoutListener listener) {///////////////////////////////////////////////
		m_doc = doc;
		m_listener = listener;
		PDFSetView(Global.def_view);
	}

	public void PDFSetBmpFormat(Bitmap.Config format) {
		if (format == Bitmap.Config.ALPHA_8)
			return;
		if (m_layout != null) {
			m_layout.vSetBmpFormat(format);
			m_bmp_format = Bitmap.Config.ALPHA_8;
			invalidate();
		} else if (m_bmp_format != format)
			m_bmp_format = format;
	}

	public void PDFGotoPage(int pageno) {
		if (m_layout == null)
			return;
		if (m_layout.vGetHeight() <= 0 || m_layout.vGetWidth() <= 0) {
			m_goto_pos = m_layout.new PDFPos();
			m_goto_pos.pageno = pageno;
			m_goto_pos.x = 0;
			m_goto_pos.y = m_doc.GetPageHeight(pageno) + 1;
		} else {
			m_layout.vGotoPage(pageno);
			invalidate();
		}
	}

	public void PDFClose() {
		if (m_layout != null) {
			PDFCancelAnnot();
			PDFEndAnnot();
			m_layout.vClose();
			m_layout = null;
			m_status = STA_NONE;
			m_zooming = false;
			m_pageno = -1;
		}
	}

	public boolean PDFIsOpen() {
		return m_layout != null;
	}

	public void OnPageChanged(int pageno) {
		m_pageno = pageno;
		if (m_listener != null)
			m_listener.OnPDFPageChanged(pageno);
	}

	public void OnPageRendered(int pageno) {
		invalidate();
	}

	public void OnFound(boolean found) {
		if (found)
			invalidate();
		else
			Toast.makeText(getContext(), "沒有更多的發現", Toast.LENGTH_SHORT).show();
	}

	public void OnPageDisplayed(Canvas canvas, VPage vpage) {
		// TODO Auto-generated method stub
	}

	public void OnTimer() {
		if (m_layout != null) {
			if (m_zooming && m_layout.vZoomEnd()) {
				m_zooming = false;
				invalidate();
			} else if (!m_layout.vRenderFinished())
				invalidate();
		}
	}

	public void PDFSetInk(int code) {
		if (code == 0)// start
		{
			m_status = STA_INK;
			m_ink = new Ink(Global.inkWidth);
		} else if (code == 1)// end
		{
			m_status = STA_NONE;
			if (m_annot_page != null) {
				Page page = m_doc.GetPage(m_annot_page.GetPageNo());
				if (page != null) {
					page.ObjsStart();
					Matrix mat = m_annot_page.CreateInvertMatrix(m_layout.vGetX(), m_layout.vGetY());
					mat.TransformInk(m_ink);
					page.AddAnnotInk(m_ink);
					mat.Destroy();
					m_layout.vRenderSync(m_annot_page);
					page.Close();
					if (m_listener != null)
						m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
				}
			}
			if (m_ink != null)
				m_ink.Destroy();
			m_ink = null;
			m_annot_page = null;
			invalidate();
		} else// cancel
		{
			m_status = STA_NONE;
			m_ink.Destroy();
			m_ink = null;
			m_annot_page = null;
			invalidate();
		}
	}

	public void PDFSetRect(int code) {
		if (code == 0)// start
		{
			m_status = STA_RECT;
		} else if (code == 1)// end
		{
			if (m_rects != null) {
				int len = m_rects.length;
				int cur;
				PDFVPageSet pset = new PDFVPageSet(len);
				for (cur = 0; cur < len; cur += 4) {
					PDFPos pos = m_layout.vGetPos((int) m_rects[cur], (int) m_rects[cur + 1]);
					VPage vpage = m_layout.vGetPage(pos.pageno);
					Page page = m_doc.GetPage(vpage.GetPageNo());
					if (page != null) {
						page.ObjsStart();
						Matrix mat = vpage.CreateInvertMatrix(m_layout.vGetX(), m_layout.vGetY());
						float rect[] = new float[4];
						if (m_rects[cur] > m_rects[cur + 2]) {
							rect[0] = m_rects[cur + 2];
							rect[2] = m_rects[cur];
						} else {
							rect[0] = m_rects[cur];
							rect[2] = m_rects[cur + 2];
						}
						if (m_rects[cur + 1] > m_rects[cur + 3]) {
							rect[1] = m_rects[cur + 3];
							rect[3] = m_rects[cur + 1];
						} else {
							rect[1] = m_rects[cur + 1];
							rect[3] = m_rects[cur + 3];
						}
						mat.TransformRect(rect);
						page.AddAnnotRect(rect, vpage.ToPDFSize(3), 0xffffff00, 0x00000000);//矩形樣式
						mat.Destroy();
						pset.Insert(vpage);
						page.Close();
					}
				}
				for (cur = 0; cur < pset.pages_cnt; cur++) {
					VPage vpage = pset.pages[cur];
					m_layout.vRenderSync(vpage);
					if (m_listener != null)
						m_listener.OnPDFPageModified(vpage.GetPageNo());
				}
			}
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		} else// cancel
		{
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		}
	}

	public void PDFSetEllipse(int code) {
		if (code == 0)// start
		{
			m_status = STA_ELLIPSE;
		} else if (code == 1)// end
		{
			if (m_rects != null) {
				int len = m_rects.length;
				int cur;
				PDFVPageSet pset = new PDFVPageSet(len);
				for (cur = 0; cur < len; cur += 4) {
					PDFPos pos = m_layout.vGetPos((int) m_rects[cur], (int) m_rects[cur + 1]);
					VPage vpage = m_layout.vGetPage(pos.pageno);
					Page page = m_doc.GetPage(vpage.GetPageNo());
					if (page != null) {
						page.ObjsStart();
						Matrix mat = vpage.CreateInvertMatrix(m_layout.vGetX(), m_layout.vGetY());
						float rect[] = new float[4];
						if (m_rects[cur] > m_rects[cur + 2]) {
							rect[0] = m_rects[cur + 2];
							rect[2] = m_rects[cur];
						} else {
							rect[0] = m_rects[cur];
							rect[2] = m_rects[cur + 2];
						}
						if (m_rects[cur + 1] > m_rects[cur + 3]) {
							rect[1] = m_rects[cur + 3];
							rect[3] = m_rects[cur + 1];
						} else {
							rect[1] = m_rects[cur + 1];
							rect[3] = m_rects[cur + 3];
						}
						mat.TransformRect(rect);
						page.AddAnnotEllipse(rect, vpage.ToPDFSize(3), 0xff00ff00, 0x00000000);//圓形樣式
						mat.Destroy();
						page.Close();
						pset.Insert(vpage);
					}
				}
				for (cur = 0; cur < pset.pages_cnt; cur++) {
					VPage vpage = pset.pages[cur];
					m_layout.vRenderSync(vpage);
					if (m_listener != null)
						m_listener.OnPDFPageModified(vpage.GetPageNo());
				}
			}
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		} else// cancel
		{
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		}
	}

	public void PDFSetSelect() {
		if (m_status == STA_SELECT) {
			m_sel_icon1.recycle();
			m_sel_icon2.recycle();
			m_sel_icon1 = null;
			m_sel_icon2 = null;
			m_annot_page = null;
			m_status = STA_NONE;
		} else {
			m_sel_icon1 = BitmapFactory.decodeResource(this.getResources(), com.radaee.viewlib.R.drawable.pt_start);
			m_sel_icon2 = BitmapFactory.decodeResource(this.getResources(), com.radaee.viewlib.R.drawable.pt_end);
			m_annot_page = null;
			m_status = STA_SELECT;
		}
	}

	public void PDFSetNote(int code) {
		if (code == 0) {
			m_note_pages = null;
			m_note_indecs = null;
			m_status = STA_NOTE;

		} else if (code == 1)// end
		{
			if (m_listener != null && m_note_pages != null) {
				int cur = 0;
				int cnt = m_note_pages.length;
				while (cur < cnt) {
					m_listener.OnPDFPageModified(m_note_pages[cur].GetPageNo());
					cur++;
				}
			}
			m_note_pages = null;
			m_note_indecs = null;
			m_status = STA_NONE;

		} else// cancel
		{
			if (m_note_pages != null)// remove added note.
			{
				int cur = 0;
				int cnt = m_note_pages.length;
				while (cur < cnt) {
					VPage vpage = m_note_pages[cur];
					Page page = m_doc.GetPage(vpage.GetPageNo());
					page.ObjsStart();
					int index = m_note_indecs[cur];
					Annotation annot;
					while ((annot = page.GetAnnot(index)) != null)
						annot.RemoveFromPage();
					page.Close();
					m_layout.vRenderSync(vpage);
					cur++;
				}
				m_note_pages = null;
				m_note_indecs = null;
				invalidate();
			}
			m_status = STA_NONE;
		}
	}

	public void PDFSetLine(int code) {
		if (code == 0)// start
		{
			m_status = STA_LINE;
		} else if (code == 1)// end
		{
			if (m_rects != null) {
				int len = m_rects.length;
				int cur;
				float[] pt1 = new float[2];
				float[] pt2 = new float[2];
				PDFVPageSet pset = new PDFVPageSet(len);
				for (cur = 0; cur < len; cur += 4) {
					PDFPos pos = m_layout.vGetPos((int) m_rects[cur], (int) m_rects[cur + 1]);
					VPage vpage = m_layout.vGetPage(pos.pageno);
					pt1[0] = m_rects[cur];
					pt1[1] = m_rects[cur + 1];
					pt2[0] = m_rects[cur + 2];
					pt2[1] = m_rects[cur + 3];
					Page page = m_doc.GetPage(vpage.GetPageNo());
					if (page != null) {
						page.ObjsStart();
						Matrix mat = vpage.CreateInvertMatrix(m_layout.vGetX(), m_layout.vGetY());
						mat.TransformPoint(pt1);
						mat.TransformPoint(pt2);
						page.AddAnnotLine(pt1, pt2, 0, 1, vpage.ToPDFSize(3), 0x80FF0000, 0x80FF0000);//三角形樣式
						mat.Destroy();
						page.Close();
						pset.Insert(vpage);
					}
				}
				for (cur = 0; cur < pset.pages_cnt; cur++) {
					VPage vpage = pset.pages[cur];
					m_layout.vRenderSync(vpage);
					if (m_listener != null)
						m_listener.OnPDFPageModified(vpage.GetPageNo());
				}
			}
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		} else// cancel
		{
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
		}
	}

	public void PDFSetStamp(int code) {
		if (code == 0)// start
		{
			m_status = STA_STAMP;
			m_icon = BitmapFactory.decodeResource(this.getResources(), com.radaee.viewlib.R.drawable.pdf_custom_stamp);
		} else if (code == 1)// end
		{
			if (m_rects != null) {
				int len = m_rects.length;
				int cur;
				PDFVPageSet pset = new PDFVPageSet(len);
				for (cur = 0; cur < len; cur += 4) {
					PDFPos pos = m_layout.vGetPos((int) m_rects[cur], (int) m_rects[cur + 1]);
					VPage vpage = m_layout.vGetPage(pos.pageno);
					Page page = m_doc.GetPage(vpage.GetPageNo());
					if (page != null) {
						Matrix mat = vpage.CreateInvertMatrix(m_layout.vGetX(), m_layout.vGetY());
						float rect[] = new float[4];
						if (m_rects[cur] > m_rects[cur + 2]) {
							rect[0] = m_rects[cur + 2];
							rect[2] = m_rects[cur];
						} else {
							rect[0] = m_rects[cur];
							rect[2] = m_rects[cur + 2];
						}
						if (m_rects[cur + 1] > m_rects[cur + 3]) {
							rect[1] = m_rects[cur + 3];
							rect[3] = m_rects[cur + 1];
						} else {
							rect[1] = m_rects[cur + 1];
							rect[3] = m_rects[cur + 3];
						}
						mat.TransformRect(rect);
						page.ObjsStart();
						page.AddAnnotBitmap(m_icon, true, rect);
						page.Close();
						mat.Destroy();
						pset.Insert(vpage);
					}
				}
				for (cur = 0; cur < pset.pages_cnt; cur++) {
					VPage vpage = pset.pages[cur];
					m_layout.vRenderSync(vpage);
					if (m_listener != null)
						m_listener.OnPDFPageModified(vpage.GetPageNo());
				}
			}
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
			if (m_icon != null)
				m_icon.recycle();
			m_icon = null;
		} else// cancel
		{
			m_status = STA_NONE;
			m_rects = null;
			invalidate();
			if (m_icon != null)
				m_icon.recycle();
			m_icon = null;
		}
	}


	public void PDFCancelAnnot() {
		if (m_status == STA_NOTE)
			PDFSetNote(2);
		if (m_status == STA_RECT)
			PDFSetRect(2);
		if (m_status == STA_INK)
			PDFSetInk(2);
		if (m_status == STA_LINE)
			PDFSetLine(2);
		if (m_status == STA_STAMP)
			PDFSetStamp(2);
		if (m_status == STA_ELLIPSE)
			PDFSetEllipse(2);
		if (m_status == STA_ANNOT)
			PDFEndAnnot();
		invalidate();
	}

	public void PDFRemoveAnnot() {
		if (m_status != STA_ANNOT || !m_doc.CanSave())
			return;
		m_annot.RemoveFromPage();
		m_annot = null;
		m_layout.vRenderSync(m_annot_page);
		if (m_listener != null)
			m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
		PDFEndAnnot();
	}

	public void PDFEndAnnot() {
		if (m_status != STA_ANNOT)
			return;
		m_annot_page = null;
		m_annot_pos = null;
		m_annot = null;
		invalidate();
		m_status = STA_NONE;
		if (m_pEdit != null && m_pEdit.isShowing())
			m_pEdit.dismiss();
		if (m_pCombo != null && m_pCombo.isShowing())
			m_pCombo.dismiss();
		if (m_listener != null)
			m_listener.OnPDFAnnotTapped(null, null);
	}

	//TODO 編輯註記
	public void PDFEditAnnot() {
		if (m_status != STA_ANNOT)
			return;
		layout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.dlg_note, null);
		subj = (EditText) layout.findViewById(R.id.txt_subj);
		content = (EditText) layout.findViewById(R.id.txt_content);
		voicebtn = (Button) layout.findViewById(R.id.voicebut);
		photobtn = (Button) layout.findViewById(R.id.photobut);
		savephbutton = (Button) layout.findViewById(R.id.savephbutton);
		vstbtn = (Button) layout.findViewById(R.id.voicestbut);
		tv = (TextView) layout.findViewById(R.id.tv);
		IV = (ImageView) layout.findViewById(R.id.imageView);
		IV.setImageBitmap(getBitmapFromSDCard(WordNum+"/"+fileNamePh));
		playbtn = (Button) layout.findViewById(R.id.PlayBut);
		player = new MediaPlayer();
		fileName = ST_ID+subj.getText()+".amr";
		fileNamePh = ST_ID+subj.getText()+".jpg";
		mDatabase = FirebaseDatabase.getInstance().getReference();	//初始化FirebaseDatabase
		AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
		final SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
		amrPlayBtn=false;
		arm_work=true;
		work_share=false;
		if (subj.getText().toString().isEmpty()){
			subj.setError("請先輸入標題");
			Toast.makeText(getContext(), "請先輸入標題", Toast.LENGTH_SHORT).show();
		}
		if (!isConnected()){
			Toast.makeText(getContext(), "請開啟網路否則無法上傳分享", Toast.LENGTH_SHORT).show();
		}
		if (isConnected()){
			Toast.makeText(getContext(), "已開啟網路可安心上傳分享", Toast.LENGTH_SHORT).show();
		}
		voicebtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				fileName = ST_ID+subj.getText()+".amr";
				try {
					if(arm_work==true){
					File SDCardpath = Environment.getExternalStorageDirectory();
					File myDataPath = new File(SDCardpath.getAbsolutePath() + "/"+WordNum);
					if (!myDataPath.exists())
						myDataPath.mkdirs();
					audioFile = new File(SDCardpath.getAbsolutePath() + "/"+WordNum+"/" + fileName);
					/*
					 * audioFile = new File( SDCardpath.getAbsolutePath() +
					 * "/download/"+ System.currentTimeMillis() + ".amr");
					 */
					mediaRecorder = new MediaRecorder();
					// 設定音源
					mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					// 設定輸出檔案的格式
					mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
					// 設定編碼格式
					mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
					// 設定錄音檔位置
					mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
					mediaRecorder.prepare();
					tv.setText("開始錄音");
					Toast.makeText(getContext(), "開始錄音", Toast.LENGTH_SHORT).show();
					// 開始錄音
					mediaRecorder.start();
						arm_work=false;
						amrPlayBtn=true;
					}
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}

		});

		// 停止錄音
		vstbtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				arm_work=false;
				amrPlayBtn=false;
				if (mediaRecorder != null) {
					tv.setText("停止錄音");
					Toast.makeText(getContext(), "停止錄音", Toast.LENGTH_SHORT).show();
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					work_share=true;
				}
			}
		});
		// 播放
		playbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				player = new MediaPlayer();
				if(amrPlayBtn==true){
					amrPlayBtn=false;
					arm_work=false;
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					work_share=true;
				}else {
					amrPlayBtn=false;
				}
				fileName = ST_ID+subj.getText()+".amr";
				File SDCardpath = Environment.getExternalStorageDirectory();
				File myDataPath = new File(SDCardpath.getAbsolutePath() + "/"+WordNum);
				if (!myDataPath.exists())
					myDataPath.mkdirs();
				audioFile = new File(SDCardpath.getAbsolutePath() + "/"+WordNum+"/" + fileName);
                ////////////////////////////////////////////共享
				//其他同學檔案路徑
				String SharedRecordingFile = MyBroadcast.StudentSelectedName+"_"+subj.getText()+".amr";
                mStorageRef = FirebaseStorage.getInstance().getReference();
                mStorageRef.child(MyBroadcast.StudentSelectedName+"/"+SharedRecordingFile).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Link =uri.toString();
						if (WordNum_btn==true) {
							try {
								player.setDataSource(Link);
							} catch (IOException e) {
								e.printStackTrace();
							}
							try {
								player.prepare();
							} catch (IOException e) {
								e.printStackTrace();
							}
							player.start();
							arm_work=true;
							//執行共享任務
						}
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
						if (WordNum_btn==true) {
							Toast.makeText(getContext(), "請開啟網路", Toast.LENGTH_SHORT).show();
						}
                    }
                 });
				tv.setText("播放錄音");
				Toast.makeText(getContext(), "播放錄音", Toast.LENGTH_SHORT).show();
				// TODO 自動產生的方法 Stub
				try {
                    if(WordNum_btn==false){
                        player.setOnCompletionListener(null);
                        player.setDataSource(audioFile.getAbsolutePath());
                        player.prepare();
						player.start();
						arm_work=true;
                        mediaRecorder.stop();
                        mediaRecorder.release();
                    }
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}
			}
		});
		photobtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				fileNamePh = ST_ID+subj.getText()+".jpg";
				//m_poto.onpoto();
				//建立 "選擇檔案 Action" 的 Intent
				Intent intent = new Intent( Intent.ACTION_GET_CONTENT);
				// 過濾檔案格式
				intent.setType( "image/*" );
				// 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
				Intent destIntent = Intent.createChooser( intent, "選擇檔案" );
				// 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
				activity.startActivityForResult( destIntent, ImageNote);
				tv.setText(fileNamePh);
				/////////////////////////////////////////////////////////////轉換圖存
			}

		});
		savephbutton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
					storageReference = FirebaseStorage.getInstance().getReference();
					DatabaseRef = FirebaseDatabase.getInstance().getReference("Image/");//到realtime database設一個圖片路徑
					String PhotoFile = MyBroadcast.StudentsName+"_"+subj.getText()+".png";//選取的圖片丟到firebase的命名
					if(activity.imgUri != null)
					{
						final ProgressDialog progressDialog = new ProgressDialog(activity);
						progressDialog.setTitle("Uploading...");
						progressDialog.show();

						//StorageReference phref = storageReference.child("Image/"+ System.currentTimeMillis() + ".png");
						phref = storageReference.child("Image/"+ MyBroadcast.StudentsName+"/"+ PhotoFile);//phref為尋找storageReference的檔案
						phref.putFile(activity.imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
							@Override
							public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
								progressDialog.dismiss();
								Toast.makeText(activity, "Uploaded", Toast.LENGTH_SHORT).show();
							}
						}).addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								progressDialog.dismiss();
								Toast.makeText(activity, "Failed"+e.getMessage(), Toast.LENGTH_SHORT).show();
							}
						}).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
							@Override
							public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
								double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
								progressDialog.setMessage("Uploaded "+(int)progress+"%");
							}
						});
						phref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
							@Override
							public void onSuccess(Uri uri) {
								String path = uri.toString();
								DownloadImageTask downloadImageTask = new DownloadImageTask();
								downloadImageTask.execute(path);
							}
						});

					}

					//分享
					String SharePhoto =  MyBroadcast.StudentSelectedName+"_"+subj.getText()+".png";
					storageReference.child("Image/" + MyBroadcast.StudentSelectedName+"/"+ SharePhoto).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
						@Override
						public void onSuccess(Uri uri) {
							String path1 = uri.toString();
							if (WordNum_btn==true){
								DownloadImageTask downloadImageTask = new DownloadImageTask();
								downloadImageTask.execute(path1);
							}
						}
					});
				}
		});

		builder.setPositiveButton("確定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				String str_subj = subj.getText().toString();
				String str_content = content.getText().toString();
				m_annot.SetPopupSubject(str_subj);
				m_annot.SetPopupText(str_content);
				Date curDate = new Date(System.currentTimeMillis()) ;
				String str_time =formatter.format(curDate);
				arm_work=true;
				dialog.dismiss();
				if(amrPlayBtn==true){
					amrPlayBtn=false;
					arm_work=false;
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					work_share=true;
				}
				if (!isConnected()){
					Toast.makeText(getContext(), "請開啟網路", Toast.LENGTH_SHORT).show();
				}
				if (isConnected()&&WordNum_btn==false&&work_share==true)
				{
					File SDCardpath = Environment.getExternalStorageDirectory();
					Uri file = Uri.fromFile(new File(SDCardpath.getAbsolutePath() + "/"+WordNum+"/" + fileName));
					StorageReference riversRef = mStorageRef.child(WordNum+"/"+file.getLastPathSegment());
					uploadTask = riversRef.putFile(file);
					uploadTask.addOnFailureListener(new OnFailureListener() {
						@Override
						public void onFailure(@NonNull Exception exception) {
							// Handle unsuccessful uploads
							Toast.makeText(getContext(), "請開啟網路", Toast.LENGTH_SHORT).show();
						}
					}).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
						@Override
						public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

							Toast.makeText(getContext(), "上傳成功加入共享", Toast.LENGTH_SHORT).show();
						}
					});

				}
				if (m_listener != null)
					m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
				PDFEndAnnot();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if(amrPlayBtn==true){
					amrPlayBtn=false;
					arm_work=false;
					mediaRecorder.stop();
					mediaRecorder.release();
					mediaRecorder = null;
					work_share=true;
				}
				dialog.dismiss();
				PDFEndAnnot();
			}
		});
		IV.setImageBitmap(getBitmapFromSDCard(WordNum+"/"+fileNamePh));
		builder.setTitle("註記內容");
		builder.setCancelable(false);
		builder.setView(layout);
		subj.setText(m_annot.GetPopupSubject());
		content.setText(m_annot.GetPopupText());
		AlertDialog dlg = builder.create();
		dlg.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		dlg.show();
		if(m_annot.GetPopupSubject() !=null || m_annot.GetPopupText() !=null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sdf.format(new java.util.Date());
			mDatabase.child("學生資料")
					.child("學生"+MyBroadcast.StudentsName+"號")
					.child("觀看分享")
					.child(date)
					.child(MyBroadcast.ShareStudentsName)
					.child(MyBroadcast.Topic)
					.child("查看註解")
					.push()
					.setValue(new StudentsNote(m_annot.GetPopupSubject(),m_annot.GetPopupText()));
		}
	}

	protected Media getContentResolver() {
		// TODO 自動產生的方法 Stub
		return null;
	}

	public void setLayout(int layout_num) {
		if (layout_num == 1) {

		}
	}

	public void PDFPerformAnnot() {
		if (m_status != STA_ANNOT)
			return;
		Page page = m_doc.GetPage(m_annot_page.GetPageNo());
		if (page == null || m_annot == null)
			return;
		page.ObjsStart();
		int dest = m_annot.GetDest();
		if (dest >= 0) {
			m_layout.vGotoPage(dest);
			invalidate();
		}
		String js = m_annot.GetJS();
		if (m_listener != null && js != null)
			m_listener.OnPDFOpenJS(js);
		String uri = m_annot.GetURI();
		if (m_listener != null && uri != null)
			m_listener.OnPDFOpenURI(uri);
		int index;
		String mov = m_annot.GetMovie();
		if (mov != null) {
			index = -1;
			if (index < 0)
				index = mov.lastIndexOf('\\');
			if (index < 0)
				index = mov.lastIndexOf('/');
			if (index < 0)
				index = mov.lastIndexOf(':');
			String save_file = Global.tmp_path + "/" + mov.substring(index + 1);
			m_annot.GetMovieData(save_file);
			if (m_listener != null)
				m_listener.OnPDFOpenMovie(save_file);
		}
		String snd = m_annot.GetSound();
		if (snd != null) {
			int paras[] = new int[4];
			index = -1;
			if (index < 0)
				index = snd.lastIndexOf('\\');
			if (index < 0)
				index = snd.lastIndexOf('/');
			if (index < 0)
				index = snd.lastIndexOf(':');
			String save_file = Global.tmp_path + "/" + snd.substring(index + 1);
			m_annot.GetSoundData(paras, save_file);
			if (m_listener != null)
				m_listener.OnPDFOpenSound(paras, save_file);
		}
		String att = m_annot.GetAttachment();
		if (att != null) {
			index = -1;
			if (index < 0)
				index = att.lastIndexOf('\\');
			if (index < 0)
				index = att.lastIndexOf('/');
			if (index < 0)
				index = att.lastIndexOf(':');
			String save_file = Global.tmp_path + "/" + att.substring(index + 1);
			m_annot.GetAttachmentData(save_file);
			if (m_listener != null)
				m_listener.OnPDFOpenAttachment(save_file);
		}
		String f3d = m_annot.Get3D();
		if (f3d != null) {
			index = -1;
			if (index < 0)
				index = f3d.lastIndexOf('\\');
			if (index < 0)
				index = f3d.lastIndexOf('/');
			if (index < 0)
				index = f3d.lastIndexOf(':');
			String save_file = Global.tmp_path + "/" + f3d.substring(index + 1);
			m_annot.Get3DData(save_file);
			if (m_listener != null)
				m_listener.OnPDFOpen3D(save_file);
		}

		int check = m_annot.GetCheckStatus();
		if (m_doc.CanSave() && check >= 0) {
			switch (check) {
			case 0:
				m_annot.SetCheckValue(true);
				break;
			case 1:
				m_annot.SetCheckValue(false);
				break;
			case 2:
			case 3:
				m_annot.SetRadio();
				break;
			}
			m_layout.vRenderSync(m_annot_page);
			if (m_listener != null)
				m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
		}

		boolean reset = m_annot.GetReset();
		if (reset && m_doc.CanSave()) {
			m_annot.SetReset();
			m_layout.vRenderSync(m_annot_page);
			if (m_listener != null)
				m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
		}
		String tar = m_annot.GetSubmitTarget();
		if (tar != null) {
			if (m_listener != null)
				m_listener.OnPDFOpenURI(tar + "?" + m_annot.GetSubmitTarget());
		}
		page.Close();
		PDFEndAnnot();
	}

	public final void PDFFindStart(String key, boolean match_case, boolean whole_word) {
		m_layout.vFindStart(key, match_case, whole_word);
	}

	public final void PDFFind(int dir) {
		m_layout.vFind(dir);
	}

	public boolean PDFSetSelMarkup(int type) {
		if (m_status == STA_SELECT && m_sel != null && m_sel.SetSelMarkup(type)) {
			m_layout.vRenderSync(m_annot_page);
			invalidate();
			if (m_listener != null)
				m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
			return true;
		} else {
			return false;
		}
	}

	public void onDismiss() {
		if (m_edit_type == 1)// edit box
		{
			EditText edit = (EditText) m_pEdit.getContentView().findViewById(com.radaee.viewlib.R.id.annot_text);
			m_annot.SetEditText(edit.getText().toString());
			m_layout.vRenderSync(m_annot_page);
			if (m_listener != null)
				m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
			PDFEndAnnot();
		}
		if (m_edit_type == 2)// combo
		{
			if (m_combo_item >= 0) {
				m_annot.SetComboItem(m_combo_item);
				m_layout.vRenderSync(m_annot_page);
				if (m_listener != null)
					m_listener.OnPDFPageModified(m_annot_page.GetPageNo());
			}
			m_combo_item = -1;
			PDFEndAnnot();
		}
		m_edit_type = 0;
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		m_combo_item = arg2;
		m_pCombo.dismiss();
	}

	public final int PDFGetCurrPage() {
		return m_pageno;
	}

	public final PDFPos PDFGetPos(int x, int y) {
		if (m_layout != null)
			return m_layout.vGetPos(0, 0);
		else
			return null;
	}

	public final void PDFSetPos(PDFPos pos, int x, int y) {
		if (m_layout != null) {
			m_layout.vSetPos(x, y, pos);
			invalidate();
		}
	}

	public void BundleSavePos(Bundle bundle) {
		if (m_layout != null) {
			PDFPos pos = m_layout.vGetPos(0, 0);
			bundle.putInt("view_page", pos.pageno);
			bundle.putFloat("view_x", pos.x);
			bundle.putFloat("view_y", pos.y);
		}
	}

	public void BundleRestorePos(Bundle bundle) {
		if (m_layout != null) {
			PDFPos pos = m_layout.new PDFPos();
			pos.pageno = bundle.getInt("view_page");
			pos.x = bundle.getFloat("view_x");
			pos.y = bundle.getFloat("view_y");
			if (m_layout.vGetHeight() <= 0 || m_layout.vGetWidth() <= 0) {
				m_goto_pos = pos;
			} else {
				m_layout.vSetPos(0, 0, pos);
				invalidate();
			}
		}
	}

	private static Bitmap getBitmapFromSDCard(String file) {
		try {
			String sd = Environment.getExternalStorageDirectory().toString();
			Bitmap bitmap = BitmapFactory.decodeFile(sd + "/" + file);
			return bitmap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    private boolean isConnected(){//是否有網路查詢方法(目前沒用到)
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

	public void startCamera() {

	}
	/*
	 * private void releaseMediaRecorder() { if (mediarecorder2 != null) {
	 * mediarecorder2.reset(); mediarecorder2.release(); mediarecorder2 = null;
	 * camera.lock(); } }
	 */

	/*
	 * private String createFilePath() { File sdCardDir =
	 * Environment.getExternalStorageDirectory(); File vrDir = new
	 * File(sdCardDir, "cw1205"); if (!vrDir.exists()) { vrDir.mkdir(); } File
	 * file = new File(vrDir, System.currentTimeMillis() + ".3gp"); String
	 * filePath = file.getAbsolutePath(); // Log.d(TAG, "輸出路徑：" + filePath);
	 * return filePath; }
	 */

	private void UploadStudentsRecordData(String data)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(new java.util.Date());
		DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("學生資料").child("學生"+MyBroadcast.StudentsName+"號").child("註記").child("圖形註記").child(date).child(data);
		databaseReference.push().setValue(1);
	}

	public final Document PDFGetDoc() {
		return m_doc;
	}

	public final boolean PDFCanSave() {
		return m_doc.CanSave();
	}

}
