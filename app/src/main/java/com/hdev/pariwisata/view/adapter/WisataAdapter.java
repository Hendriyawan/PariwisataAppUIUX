package com.hdev.pariwisata.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.hdev.pariwisata.R;
import com.hdev.pariwisata.view.model.Wisata;
import com.hdev.pariwisata.view.utils.AssetManager;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 12 agustus 2019
 *
 * @author gdev (Hendriyawan)
 */
public class WisataAdapter extends RecyclerView.Adapter<WisataAdapter.WisataHolder> {
    private Context context;
    private List<Wisata> wisataList;
    private OnWisataClick onWisataClick;

    public WisataAdapter(Context context, List<Wisata> wisataList) {
        this.context = context;
        this.wisataList = wisataList;
    }

    public void setOnWisataClick(OnWisataClick onWisataClick) {
        if(onWisataClick != null){
            this.onWisataClick = onWisataClick;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull WisataHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.view.clearAnimation();
    }

    @NonNull
    @Override
    public WisataHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new WisataHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_wisata, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WisataHolder wisataHolder, int position) {
        final Wisata wisata = wisataList.get(position);
        String[] foto = wisata.getFoto().split(",");
        wisataHolder.imageViewFotoWisata.setImageDrawable(AssetManager.drawableFromAsset(context, foto[0]));
        wisataHolder.textViewNamaWisata.setText(wisata.getNama());
        wisataHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onWisataClick.onClick(wisata);
            }
        });
        wisataHolder.view.setAnimation(AnimationUtils.loadAnimation(context, R.anim.zoom_in));

    }

    @Override
    public int getItemCount() {
        return wisataList != null ? wisataList.size() : 0;
    }

    //interface
    public interface OnWisataClick {
        void onClick(Wisata wisata);
    }

    //class Holder
    class WisataHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_foto_wisata)
        ImageView imageViewFotoWisata;
        @BindView(R.id.item_nama_wisata)
        TextView textViewNamaWisata;
        View view;

        public WisataHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            this.view = itemView;
        }
    }
}
