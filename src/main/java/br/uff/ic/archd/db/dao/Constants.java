/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.uff.ic.archd.db.dao;

/**
 *
 * @author wallace
 */
public class Constants {
    public final static String DB_DIR = System.getProperty("user.home") + "/.archDB5/METRIC_VALUE_DB_DIR/";
    
    public final static int ANOMALIE_GOD_PACKAGE = 1;
    public final static int ANOMALIE_GOD_CLASS = 2;
    public final static int ANOMALIE_MISPLACED_CLASS = 3;
    public final static int ANOMALIE_FEATURE_ENVY = 4;
    public final static int ANOMALIE_SHOTGUN_SURGERY = 5;
    public final static int ANOMALIE_GOD_METHOD = 6;
    
    public final static int ANOMALIE_TYPE_CONGENITAL_NEVER_CORRECTED = 1;
    public final static int ANOMALIE_TYPE_CONGENITAL_NOT_CORRECTED_BUT_CORRECTED_ONE_TIME = 2;
    public final static int ANOMALIE_TYPE_CONGENITAL_NOT_CORRECTED_RECURRENT_CORRECTED = 3;
    public final static int ANOMALIE_TYPE_CONGENITAL_CORRECTED = 4;
    public final static int ANOMALIE_TYPE_CONGENITAL_CORRECTED_BUT_CORRECTED_UM_TIME_BEFORE = 5;
    public final static int ANOMALIE_TYPE_CONGENITAL_CORRECTED_RECURRENT_CORRECTED = 6;
    public final static int ANOMALIE_TYPE_ADQUIRED_NEVER_CORRECTED = 7;
    public final static int ANOMALIE_TYPE_ADQUIRED_NOT_CORRECTED_BUT_CORRECTED_ONE_TIME = 8;
    public final static int ANOMALIE_TYPE_ADQUIRED_NOT_CORRECTED_RECURRENT_CORRECTED = 9;
    public final static int ANOMALIE_TYPE_ADQUIRED_CORRECTED = 10;
    public final static int ANOMALIE_TYPE_ADQUIRED_CORRECTED_BUT_CORRECTED_UM_TIME_BEFORE = 11;
    public final static int ANOMALIE_TYPE_ADQUIRED_CORRECTED_RECURRENT_CORRECTED = 12;
}
