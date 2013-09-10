package com.preeti.mykindleapp;

import org.apache.pdfbox.exceptions.InvalidPasswordException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.PDFTextStripperByArea;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class HomePageActivity extends Activity implements View.OnClickListener{

	private int REQUEST_PICK_FILE=101;
	private Button button;
	private WebView webView;
	private PDDocument document;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		button = (Button)findViewById(R.id.page_button);
		webView = (WebView)findViewById(R.id.web_view);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	@Override
	public void onClick(View v) {

		Intent intent = new Intent();
		intent.setType("application/pdf");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Pdf"), REQUEST_PICK_FILE );

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUEST_PICK_FILE)
			switch (resultCode){
			case Activity.RESULT_OK:
				//
				Uri f = data.getData();

                if(f.getLastPathSegment().endsWith("pdf")){
                	readPdfFile(f);
                } else {
                    Toast.makeText(this, "Invalid file type", Toast.LENGTH_SHORT).show();   
                }  
				
				break;
			case Activity.RESULT_CANCELED:
			default : 
				Toast.makeText(HomePageActivity.this, "could not open the file", Toast.LENGTH_SHORT).show();
			}
	}

	private void readPdfFile(Uri f ) {
		try
		{
			document = PDDocument.load(f.getPath());
			document.getClass();
			if( document.isEncrypted() )
			{
				try
				{
					document.decrypt( "" );
				}
				catch( InvalidPasswordException e )
				{
					System.err.println( "Error: Document is encrypted with a password." );
					System.exit( 1 );
				}
			}

			PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition( true );
			PDFTextStripper strippe = new PDFTextStripper();
			String st = strippe.getText(document);

			System.out.println(st);
			webView.getSettings().setJavaScriptEnabled(true);
			   //webView.loadUrl("http://www.google.com");
		 
			   String customHtml = "<html><body><h1>"+st+"</h1></body></html>";
			   webView.loadData(customHtml, "text/html", "UTF-8");

		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
}
