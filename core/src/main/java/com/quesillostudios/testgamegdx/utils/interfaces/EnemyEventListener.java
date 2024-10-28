package com.quesillostudios.testgamegdx.utils.interfaces;

import com.quesillostudios.testgamegdx.entities.EnemyShip;
import com.quesillostudios.testgamegdx.entities.interfaces.Damagable;

public interface EnemyEventListener extends EventListener {
    void onKill(Damagable enemy, boolean bonus);
}
