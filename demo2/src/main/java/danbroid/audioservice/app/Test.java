package danbroid.audioservice.app;

import androidx.compose.material.icons.Icons;
import androidx.compose.ui.graphics.vector.ImageVector;

import danbroid.logging.DBLog;

public class Test {
  public void test() {
    DBLog log = Logging.INSTANCE.getJlog();

    log.dinfo("test()!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!", null);
    try {

      log.dinfo("FILLED CLASS: " + Icons.Filled.INSTANCE.getClass(), null);
      String icon = "Savings";
      String iconType = "filled";
      String clsName = "androidx.compose.material.icons." + iconType + "." + icon + "Kt";
      ImageVector iconVector = (ImageVector) Class.forName(clsName).getMethod("get" + icon, Icons.Filled.class).invoke(null, Icons.Filled.INSTANCE);
      log.dinfo("GOT IMAGE VECTOR: " + iconVector, null);



      log.derror("FILLED CLASS: " + Icons.Filled.class, null);
    } catch (Throwable e) {
      log.derror("FAILED: " + e.getMessage(), e);
    }
  }
}


