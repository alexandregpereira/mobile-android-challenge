package br.alexandregpereira.amaro.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public interface Navigator {
    FragmentTransaction navigateTo(@NonNull Fragment fragment);
}
