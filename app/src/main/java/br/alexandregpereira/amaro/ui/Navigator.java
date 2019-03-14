package br.alexandregpereira.amaro.ui;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public interface Navigator {
    void navigateTo(@NonNull Fragment fragment);
}
