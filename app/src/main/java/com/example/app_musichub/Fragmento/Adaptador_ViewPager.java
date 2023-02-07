package com.example.app_musichub.Fragmento;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class Adaptador_ViewPager extends FragmentPagerAdapter {

    private final ArrayList<Fragment> fragmentos = new ArrayList<>();
    private final ArrayList<String> titulos_fragmentos = new ArrayList<>();

    public Adaptador_ViewPager(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentos.get(position);
    }

    @Override
    public int getCount() {
        return fragmentos.size();
    }

    public void agregarFragmento(Fragment fragmento, String titulo) {
        fragmentos.add(fragmento);
        titulos_fragmentos.add(titulo);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titulos_fragmentos.get(position);
    }
}
