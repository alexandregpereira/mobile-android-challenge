package br.alexandregpereira.amaro.product;

import androidx.transition.ChangeBounds;
import androidx.transition.ChangeTransform;
import androidx.transition.TransitionSet;

public class DetailsTransition extends TransitionSet {

    public DetailsTransition() {
        setOrdering(TransitionSet.ORDERING_TOGETHER);
        addTransition(new ChangeBounds()).
                addTransition(new ChangeTransform());
    }
}
