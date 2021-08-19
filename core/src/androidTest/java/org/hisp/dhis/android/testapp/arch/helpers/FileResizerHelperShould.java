/*
 *  Copyright (c) 2004-2021, University of Oslo
 *  All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are met:
 *  Redistributions of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 *
 *  Redistributions in binary form must reproduce the above copyright notice,
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 *  Neither the name of the HISP project nor the names of its contributors may
 *  be used to endorse or promote products derived from this software without
 *  specific prior written permission.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 *  ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 *  WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 *  ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 *  (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 *  LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 *  (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.hisp.dhis.android.testapp.arch.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import org.hisp.dhis.android.core.arch.helpers.FileResizerHelper;
import org.hisp.dhis.android.core.arch.helpers.FileResourceDirectoryHelper;
import org.hisp.dhis.android.core.maintenance.D2Error;
import org.hisp.dhis.android.core.utils.runner.D2JunitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import static com.google.common.truth.Truth.assertThat;

@RunWith(D2JunitRunner.class)
public class FileResizerHelperShould {

    @Test
    public void resize_to_small_file() throws D2Error {
        File file = getFile(Bitmap.CompressFormat.PNG, getBitmap(2048, 1024));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        assertThat(bitmap.getHeight()).isEqualTo(1024);
        assertThat(bitmap.getWidth()).isEqualTo(2048);

        File resizedFile = FileResizerHelper.resizeFile(file, FileResizerHelper.Dimension.SMALL);
        Bitmap resizedBitmap = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

        assertThat(resizedBitmap.getHeight()).isEqualTo(128);
        assertThat(resizedBitmap.getWidth()).isEqualTo(256);
    }

    @Test
    public void resize_to_medium_file() throws D2Error {
        File file = getFile(Bitmap.CompressFormat.PNG, getBitmap(2048, 1024));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        assertThat(bitmap.getHeight()).isEqualTo(1024);
        assertThat(bitmap.getWidth()).isEqualTo(2048);

        File resizedFile = FileResizerHelper.resizeFile(file, FileResizerHelper.Dimension.MEDIUM);
        Bitmap resizedBitmap = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

        assertThat(resizedBitmap.getHeight()).isEqualTo(256);
        assertThat(resizedBitmap.getWidth()).isEqualTo(512);
    }

    @Test
    public void resize_to_large_file() throws D2Error {
        File file = getFile(Bitmap.CompressFormat.PNG, getBitmap(2048, 1024));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        assertThat(bitmap.getHeight()).isEqualTo(1024);
        assertThat(bitmap.getWidth()).isEqualTo(2048);

        File resizedFile = FileResizerHelper.resizeFile(file, FileResizerHelper.Dimension.LARGE);
        Bitmap resizedBitmap = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

        assertThat(resizedBitmap.getHeight()).isEqualTo(512);
        assertThat(resizedBitmap.getWidth()).isEqualTo(1024);
    }

    @Test
    public void resize_jpeg() throws D2Error {
        File file = getFile(Bitmap.CompressFormat.JPEG, getBitmap(2048, 1024));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        assertThat(bitmap.getHeight()).isEqualTo(1024);
        assertThat(bitmap.getWidth()).isEqualTo(2048);

        File resizedFile = FileResizerHelper.resizeFile(file, FileResizerHelper.Dimension.SMALL);
        Bitmap resizedBitmap = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

        assertThat(resizedBitmap.getHeight()).isEqualTo(128);
        assertThat(resizedBitmap.getWidth()).isEqualTo(256);
    }

    @Test
    public void do_not_resize_small_to_large_file() throws D2Error {
        File file = getFile(Bitmap.CompressFormat.PNG, getBitmap(100, 125));
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());

        assertThat(bitmap.getHeight()).isEqualTo(125);
        assertThat(bitmap.getWidth()).isEqualTo(100);

        File resizedFile = FileResizerHelper.resizeFile(file, FileResizerHelper.Dimension.LARGE);
        Bitmap resizedBitmap = BitmapFactory.decodeFile(resizedFile.getAbsolutePath());

        assertThat(resizedBitmap.getHeight()).isEqualTo(125);
        assertThat(resizedBitmap.getWidth()).isEqualTo(100);
    }

    private static File getFile(Bitmap.CompressFormat compressFormat, Bitmap bitmap) {
        Context context = InstrumentationRegistry.getInstrumentation().getContext();
        File imageFile = new File(FileResourceDirectoryHelper.getFileResourceDirectory(context), "image." +
                compressFormat.name().toLowerCase());
        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(compressFormat, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(FileResizerHelperShould.class.getSimpleName(), "Error writing bitmap", e);
        }
        return imageFile;
    }

    private static Bitmap getBitmap(int width, int height) {
        return Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    }
}
