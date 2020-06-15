package com.xmwdkk.boothprint;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.xmwdkk.boothprint.base.App;
import com.xmwdkk.boothprint.print.GPrinterCommand;
import com.xmwdkk.boothprint.print.PrintPic;
import com.xmwdkk.boothprint.print.PrintQueue;
import com.xmwdkk.boothprint.print.PrintUtil;
import com.xmwdkk.boothprint.printutil.PrintOrderDataMaker;
import com.xmwdkk.boothprint.printutil.PrinterWriter;
import com.xmwdkk.boothprint.printutil.PrinterWriter58mm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by liuguirong on 8/1/17.
 * <p/>
 * print ticket service
 */
public class BtService extends IntentService {

    public BtService() {
        super("BtService");
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public BtService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST)) {
            printTest();
        } else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_TEST_TWO)) {
            printTesttwo(3);
        }else if (intent.getAction().equals(PrintUtil.ACTION_PRINT_BITMAP)) {
            printBitmapTest();
        }

    }

    private void printTest() {
            PrintOrderDataMaker printOrderDataMaker = new PrintOrderDataMaker(
                    this,"", PrinterWriter58mm.TYPE_58, PrinterWriter.HEIGHT_PARTING_DEFAULT);
            ArrayList<byte[]> printData = (ArrayList<byte[]>) printOrderDataMaker.getPrintData(PrinterWriter58mm.TYPE_58);
            PrintQueue.getQueue(getApplicationContext()).add(printData);

    }

    /**
     * 打印几遍
     * @param num
     */
      private void printTesttwo(int num) {
        try {
            ArrayList<byte[]> bytes = new ArrayList<byte[]>();
            for (int i = 0; i < num; i++) {
                String message = "蓝牙打印测试\n蓝牙打印测试\n蓝牙打印测试\n\n";
                bytes.add(GPrinterCommand.reset);
                bytes.add(message.getBytes("gbk"));
                bytes.add(GPrinterCommand
                        .print);
                bytes.add(GPrinterCommand.print);
                bytes.add(GPrinterCommand.print);
            }
            PrintQueue.getQueue(getApplicationContext()).add(bytes);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void print(byte[] byteArrayExtra) {
        if (null == byteArrayExtra || byteArrayExtra.length <= 0) {
            return;
        }
        PrintQueue.getQueue(getApplicationContext()).add(byteArrayExtra);
    }

    private void printBitmapTest() {
        /*BufferedInputStream bis;
        try {
            bis = new BufferedInputStream(getAssets().open(
                    "btn_qrcode.png"));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }*/
        ArrayList<byte[]> data = new ArrayList<>();
        PrinterWriter58mm printer;
        try {
            printer = new PrinterWriter58mm(PrinterWriter.HEIGHT_PARTING_DEFAULT,PrinterWriter58mm.TYPE_58 );
            data.add(printer.getDataAndReset());
//            printer.printLine();
            //换行
            printer.printLineFeed();
            printer.setAlignCenter();
//            printer.printDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.demo));
//            printer.getImageByte(ContextCompat.getDrawable(getApplicationContext(), R.drawable.order));

            printer.print("test");
            printer.printLineFeed();
//            printer.printLine();
//            printer.printLineFeed();
//            printer.setAlignCenter();
//            printer.print("-结束line为手写-");
//            printer.printLineFeed();
//            printer.print("--------------------------------");
            data.add(printer.getDataAndReset());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
//        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.demo);
////        Bitmap bitmap = BitmapFactory.decodeStream(bis);
//        PrintPic printPic = PrintPic.getInstance();
//        printPic.init(bitmap);
//        if (null != bitmap) {
//            if (bitmap.isRecycled()) {
//                bitmap = null;
//            } else {
//                bitmap.recycle();
//                bitmap = null;
//            }
//        }
//        byte[] bytes = printPic.printDraw();
//
//
//        data.add(bytes);
        try {
            printer.printLineFeed();
            printer.printLineFeed();
//            printer.feedPaperCutPartial();
            data.add(printer.getDataAndClose());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintQueue.getQueue(getApplicationContext()).add(data);
    }
}