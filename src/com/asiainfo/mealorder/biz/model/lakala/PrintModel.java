package com.asiainfo.mealorder.biz.model.lakala;

import com.lkl.cloudpos.aidl.printer.PrintItemObj;

import java.util.List;

/**
 * Created by gjr on 2016/8/23 16:08.
 * mail : gjr9596@gmail.com
 */
public interface PrintModel {

    List<PrintItemObj> getPrintData();
}
