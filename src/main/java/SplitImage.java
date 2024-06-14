import org.apache.commons.imaging.ImageInfo;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;

import java.io.File;
import java.io.IOException;

/**
 * @className: SplitImage
 * @Description: TODO
 * @version: v1.0.0
 * @author: wzx
 * @date: 2024/6/14 下午 1:51
 */


public class SplitImage {

    public static void main(String[] args) {
        // 定义图片文件夹路径
        String imageFolderPath = "C:\\Users\\mowang\\OneDrive\\图片";
        String horizontalFolderPath = "C:\\h";
        String verticalFolderPath = "C:\\c";

        File imageFolder = new File(imageFolderPath);
        File horizontalFolder = new File(horizontalFolderPath);
        File verticalFolder = new File(verticalFolderPath);

        // 创建目标文件夹，如果不存在
        if (!horizontalFolder.exists()) {
            horizontalFolder.mkdirs();
        }
        if (!verticalFolder.exists()) {
            verticalFolder.mkdirs();
        }

        // 递归遍历文件夹及其子文件夹中的所有图片
        processFolder(imageFolder, horizontalFolder, verticalFolder);
    }

    // 递归处理文件夹
    private static void processFolder(File folder, File horizontalFolder, File verticalFolder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归处理子文件夹
                    processFolder(file, horizontalFolder, verticalFolder);
                } else {
                    try {
                        if (isImageFile(file)) {
                            ImageInfo imageInfo = Imaging.getImageInfo(file);
                            int width = imageInfo.getWidth();
                            int height = imageInfo.getHeight();

                            String newFileName = System.currentTimeMillis() + "_" + file.getName();
                            File destinationFile;

                            if (width > height) {
                                // 如果宽度大于高度，认为是横图
                                destinationFile = new File(horizontalFolder, newFileName);
                            } else {
                                // 否则认为是竖图
                                destinationFile = new File(verticalFolder, newFileName);
                            }

                            // 移动并重命名文件
                            if (file.renameTo(destinationFile)) {
                                System.out.println("成功移动文件: " + file.getName() + " 到 " + destinationFile.getPath());
                            } else {
                                System.out.println("无法移动文件: " + file.getName());
                            }
                        }
                    } catch (IOException | ImageReadException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // 检查文件是否为图片
    private static boolean isImageFile(File file) {
        String[] imageExtensions = new String[]{"png", "jpg", "jpeg", "bmp", "gif"};
        String fileName = file.getName().toLowerCase();
        for (String extension : imageExtensions) {
            if (fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}