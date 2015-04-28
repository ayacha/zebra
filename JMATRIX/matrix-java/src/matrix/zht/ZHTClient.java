/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 2.0.11
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package matrix.zht;

public class ZHTClient {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected ZHTClient(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(ZHTClient obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        cpp_zhtclientJNI.delete_ZHTClient(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public ZHTClient() {
    this(cpp_zhtclientJNI.new_ZHTClient__SWIG_0(), true);
  }

  public ZHTClient(String zhtConf, String neighborConf) {
    this(cpp_zhtclientJNI.new_ZHTClient__SWIG_1(zhtConf, neighborConf), true);
  }

  public int init(String zhtConf, String neighborConf) {
    return cpp_zhtclientJNI.ZHTClient_init(swigCPtr, this, zhtConf, neighborConf);
  }

  public String lookup(String key) {
    return cpp_zhtclientJNI.ZHTClient_lookup(swigCPtr, this, key);
  }

  public int insert(String key, String val) {
    return cpp_zhtclientJNI.ZHTClient_insert(swigCPtr, this, key, val);
  }

  public int compare_swap_int(String key, String seen_val, String new_val) {
    return cpp_zhtclientJNI.ZHTClient_compare_swap_int(swigCPtr, this, key, seen_val, new_val);
  }

  public String compare_swap_string(String key, String seen_val, String new_val) {
    return cpp_zhtclientJNI.ZHTClient_compare_swap_string(swigCPtr, this, key, seen_val, new_val);
  }

}
