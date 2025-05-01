package com.example.goodluck.global;

import java.util.UUID;

public class FilePathHelper{
    public static String generateProfileName(String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        return String.format("profiles/%s.%s", uuid, getExtension(originalFileName));
    }

    public static String generateAttachName(Long no, String originalFileName) {
        String uuid = UUID.randomUUID().toString();
        return String.format("attaches/%d/%s.%s", no, uuid, getExtension(originalFileName));
    };

    // 확장자만 추출
    public static String getExtension(String originalFileName) {
        int idx = originalFileName.lastIndexOf(".");
        if(idx == -1) {
            return "";
        }
        return originalFileName.substring(idx + 1);
    }

    // 파일 명만 추출
    public static String getFileNameOlny(String fullFilePathName){
        if (fullFilePathName == null || fullFilePathName.isEmpty()) {
            return "";
        }
        // 1. 마지막 / 뒤 파일명 추출
        fullFilePathName = fullFilePathName.replace("\\", "/"); // 윈도우 호환
        int lastSlashIdx = fullFilePathName.lastIndexOf('/');
        String fileName = (lastSlashIdx != -1) ? fullFilePathName.substring(lastSlashIdx + 1) : fullFilePathName;

        // 2. 파일명에서 확장자 제거
        int dotIdx = fileName.lastIndexOf('.');
        return (dotIdx != -1) ? fileName.substring(0, dotIdx) : fileName;
    }

    // 디렉터리 경로만 추출 (상대경로)
    public static String getDirectoryPath(String fullFilePathName) {
        if (fullFilePathName == null || fullFilePathName.isEmpty()) {
            return "";
        }
        fullFilePathName = fullFilePathName.replace("\\", "/");
        int idx = fullFilePathName.lastIndexOf('/');
        return (idx != -1) ? fullFilePathName.substring(0, idx) : "";
    }

}