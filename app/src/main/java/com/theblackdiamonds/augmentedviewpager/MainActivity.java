package com.theblackdiamonds.augmentedviewpager;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.ar.core.Anchor;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArFragment arFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arFragment);

        assert arFragment != null;
        arFragment.setOnTapArPlaneListener(((hitResult, plane, motionEvent) ->
                addSceneToPlane(hitResult.createAnchor())
        ));
    }

    private void addSceneToPlane(Anchor anchor) {
        ViewRenderable.builder().setView(MainActivity.this, R.layout.test).build()
                .thenAccept(viewRenderable -> {
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setRenderable(viewRenderable);
                    arFragment.getArSceneView().getScene().addChild(anchorNode);

                    View view = viewRenderable.getView();
                    ViewPager viewPager = view.findViewById(R.id.viewPager);

                    List<Integer> images = new ArrayList<>();
                    images.add(R.drawable.ic_launcher_background);
                    images.add(R.drawable.ic_launcher_foreground);
                    images.add(R.drawable.ic_launcher_background);
                    images.add(R.drawable.ic_launcher_foreground);
                    images.add(R.drawable.ic_launcher_background);
                    images.add(R.drawable.ic_launcher_foreground);

                    Adapter adapter = new Adapter(images);
                    viewPager.setAdapter(adapter);
                });
    }

    public class Adapter extends PagerAdapter {

        List<Integer> images;

        Adapter(List<Integer> images) {
            this.images = images;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = getLayoutInflater().inflate(R.layout.item, container, false);

            ImageView imageView = view.findViewById(R.id.imageView);
            imageView.setImageResource(images.get(position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((ImageView) object);
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}