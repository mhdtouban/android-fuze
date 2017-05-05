package io.gmas.fuze.commons.utils;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

public class FrescoUtils {

    public static void stream(final @NonNull Uri uri, final @NonNull SimpleDraweeView view) {
        view.setController(buildDraweeController(uri, view));
    }

    private static ImageRequest buildImageRequest(final @NonNull Uri uri) {
        return ImageRequestBuilder.newBuilderWithSource(uri)
                .setProgressiveRenderingEnabled(true)
                .build();
    }

    private static DraweeController buildDraweeController(final @NonNull Uri uri, final @NonNull SimpleDraweeView view) {
        return Fresco.newDraweeControllerBuilder()
                .setImageRequest(buildImageRequest(uri))
                .setOldController(view.getController())
                .build();
    }

}
