package com.danxx.androidthread;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;

public class JavaIOActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	/**
	  * 获取网络图片
	  * 
	  * @param urlString
	  *            如：http://f.hiphotos.baidu.com/image/w%3D2048/sign=3
	  *            b06d28fc91349547e1eef6462769358
	  *            /d000baa1cd11728b22c9e62ccafcc3cec2fd2cd3.jpg
	  *  Android4.0之后对网络的操作不能放在UI线程,我们可以自己写一个线程来处理
	  */
	 public static Bitmap getNetWorkBitmap(String urlString) {
		 
		 URL imgUrl = null;
		 Bitmap bitmap = null;
		 try {
			imgUrl = new URL(urlString);
			/**使用HttpURLConnetion打开网络连接**/
			HttpURLConnection urlConn = (HttpURLConnection) imgUrl.openConnection();
			/**设置连接超时**/
			urlConn.setReadTimeout(5*1000);
			/**设置请求方式**/
			urlConn.setRequestMethod("GET");
			urlConn.connect();
			
			/**将得到的数据转换为InputStream输入流**/
			InputStream is = urlConn.getInputStream();
			/**讲ImputStream转为Bitmap**/
			bitmap = BitmapFactory.decodeStream(is);
			/**关闭流**/
			is.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		 
		return bitmap;
		 
	 }
	 /**
	  * 保存bitmap到本地
	  * @param mp 要保存的bitmap
	  * @param fileName  
	 * @throws IOException 
	  */
	 public void saveFile(Bitmap mp ,String fileName) throws IOException{
		 /**保存的路径**/
		 String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_test/";
		 File dirFile = new File(ALBUM_PATH);
		 if(!dirFile.exists()){  //如果目录不存在就生成
			 dirFile.mkdir();
		 }
		 /**我们的图片以这样的路径和名字保存**/
		 File mFile = new File(ALBUM_PATH + fileName);
		 /**向mFlie建立输出流**/
		 BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(mFile));
		 /**压缩bitmap并连接到输出流**/
		 mp.compress(CompressFormat.JPEG, 80, bos);
		 bos.flush();
		 bos.close();
	 }
	
}
