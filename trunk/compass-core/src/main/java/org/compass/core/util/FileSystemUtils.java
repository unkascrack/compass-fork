/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.compass.core.util;

import java.io.File;
import java.io.IOException;

/**
 * @author kimchy
 */
public abstract class FileSystemUtils {

    /**
     * Delete the supplied {@link java.io.File} - for directories,
     * recursively delete any nested directories or files as well.
     *
     * @param root the root <code>File</code> to delete
     * @return <code>true</code> if the <code>File</code> was deleted,
     *         otherwise <code>false</code>
     */
    public static boolean deleteRecursively(File root) {
        if (root != null && root.exists()) {
            if (root.isDirectory()) {
                File[] children = root.listFiles();
                if (children != null) {
                    for (File aChildren : children) {
                        deleteRecursively(aChildren);
                    }
                }
            }
            return root.delete();
        }
        return false;
    }

    /**
     * Recursively copy the contents of the <code>src</code> file/directory
     * to the <code>dest</code> file/directory.
     *
     * @param src  the source directory
     * @param dest the destination directory
     * @throws java.io.IOException in the case of I/O errors
     */
    public static void copyRecursively(File src, File dest) throws IOException {
        Assert.isTrue(src != null && (src.isDirectory() || src.isFile()), "Source File must denote a directory or file");
        Assert.notNull(dest, "Destination File must not be null");
        doCopyRecursively(src, dest);
    }

    /**
     * Actually copy the contents of the <code>src</code> file/directory
     * to the <code>dest</code> file/directory.
     *
     * @param src  the source directory
     * @param dest the destination directory
     * @throws java.io.IOException in the case of I/O errors
     */
    private static void doCopyRecursively(File src, File dest) throws IOException {
        if (src.isDirectory()) {
            dest.mkdir();
            File[] entries = src.listFiles();
            if (entries == null) {
                throw new IOException("Could not list files in directory: " + src);
            }
            for (File file : entries) {
                doCopyRecursively(file, new File(dest, file.getName()));
            }
        } else if (src.isFile()) {
            try {
                dest.createNewFile();
            }
            catch (IOException ex) {
                IOException ioex = new IOException("Failed to create file: " + dest);
                ioex.initCause(ex);
                throw ioex;
            }
            CopyUtils.copy(src, dest);
        } else {
            // Special File handle: neither a file not a directory.
            // Simply skip it when contained in nested directory...
        }
    }

}