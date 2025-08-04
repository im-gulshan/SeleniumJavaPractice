package MainUtilities.JMeter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JMeterUtils {//22_July
    public static void extractZipFile(String zipFilePath, String desDir) throws Exception{
        File dir = new File(desDir);
        if (!dir.exists()) dir.mkdir();

        byte[] buffer = new byte[1024];
        try(ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFilePath))){
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null){
                File newFile = newFile(dir, zipEntry);
                if(zipEntry.isDirectory()){
                    newFile.mkdir();
                } else {
                    new File(newFile.getParent()).mkdir();
                    try (FileOutputStream fos = new FileOutputStream(newFile)){
                        int len;
                        while ((len = zis.read(buffer)) > 0){
                            fos.write(buffer, 0, len);
                        }
                    }
                }
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        }

    }

    private static File newFile(File destinationDir, ZipEntry zipEntry) throws Exception{
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath+File.separator)){
            throw new IOException("Entry is outside of the targer dir: "+zipEntry.getName());
        }
        return destFile;
    }

    public static void createWindowsShortcut(String targetPath, String shortcutName) throws IOException {
        String desktopPath = System.getProperty("user.home") + "\\Desktop";
        String shortcutPath = desktopPath + "\\" + shortcutName + ".lnk";

        String vbsScript =
                "Set oWS = WScript.CreateObject(\"WScript.Shell\")\n" +
                        "sLinkFile = \"" + shortcutPath.replace("\\", "\\\\") + "\"\n" +
                        "Set oLink = oWS.CreateShortcut(sLinkFile)\n" +
                        "oLink.TargetPath = \"" + targetPath.replace("\\", "\\\\") + "\"\n" +
                        "oLink.WorkingDirectory = \"" + new File(targetPath).getParent().replace("\\", "\\\\") + "\"\n" +
                        "oLink.WindowStyle = 1\n" +
                        "oLink.Save\n";

        File tempVbs = File.createTempFile("createShortcut", ".vbs");
        try (FileWriter fw = new FileWriter(tempVbs)) {
            fw.write(vbsScript);
        }

        Runtime.getRuntime().exec("wscript " + tempVbs.getAbsolutePath());
    }

    public static String findExtractedJMeterFolder(String baseDirPath) {
        File baseDir = new File(baseDirPath);
        File[] matches = baseDir.listFiles(file -> file.isDirectory() && file.getName().startsWith("apache-jmeter-"));
        if (matches != null && matches.length > 0) {
            return matches[0].getAbsolutePath();
        }
        throw new RuntimeException("JMeter folder not found in " + baseDirPath);
    }


} // JMeterUtils
