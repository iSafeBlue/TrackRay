package com.trackray.base.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * IO流常规解析类
 * @author fre1Ray
 * */
public class IOUtils {

	/**
	 * 解析Process类
	 * @throws IOException 
	 * */
	public static String analysisProcess(Process process) throws IOException{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder(); 
		
		try {
			br = new BufferedReader(new InputStreamReader(process.getInputStream(),Charset.forName("GBK")));
			String line = null;
		//	System.out.println("打印信息");
			
			while((line=br.readLine())!=null){  
				sb.append(line + "\n"); 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			br.close();
		}
		
		return sb.toString();
	}
	public static String analysisStream(InputStream stream) throws IOException{
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		try {
			br = new BufferedReader(new InputStreamReader(stream));
			String line = null;

			while((line=br.readLine())!=null){
				sb.append(line + "\n");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}finally {
			br.close();
		}

		return sb.toString();
	}

	public static BufferedReader streamToReader(InputStream s){
		BufferedReader br = null;

		try {
			br = new BufferedReader(new InputStreamReader(s));
		} catch (Exception e) {
			e.printStackTrace();
		}finally {

		}

		return br;
	}



	
	
}
