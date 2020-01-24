package espol.edu.bagtraking.Modelo;

import java.io.IOException;

public class Metodos {
    public static boolean isOnline() {


        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");

            int mExitValue = mIpAddrProcess.waitFor();


            if (mExitValue == 0 ) {
                return true;
            } else {
                Process mIpAddrProcess2 = runtime.exec("/system/bin/ping -c 1 200.126.14.129");
                int mExitValue2 = mIpAddrProcess2.waitFor();
                if (mExitValue2 == 0) {
                    return true;
                }
                return false;
            }
        } catch (InterruptedException ignore) {
            ignore.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        }
        return false;
    }

}
