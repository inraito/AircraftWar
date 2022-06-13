package edu.hitsz.aircraftwar.game.repository.difficulty;

import javax.inject.Inject;
import javax.inject.Singleton;

import edu.hitsz.aircraftwar.game.repository.difficulty.template.BaseTemplate;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.EasyTemplate;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.HardTemplate;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.MultTemplate;
import edu.hitsz.aircraftwar.game.repository.difficulty.template.NormalTemplate;

@Singleton
public class TemplateManager {
    private final BaseTemplate easy;
    private final BaseTemplate normal;
    private final BaseTemplate hard;
    private final BaseTemplate mult;

    @Inject
    public TemplateManager(EasyTemplate easy, NormalTemplate normal, HardTemplate hard, MultTemplate mult){
        this.easy = easy;
        this.normal = normal;
        this.hard = hard;
        this.mult = mult;
    }

    public BaseTemplate getEasyTemplate() {
        return easy;
    }

    public BaseTemplate getNormalTemplate() {
        return normal;
    }

    public BaseTemplate getHardTemplate() {
        return hard;
    }

    public BaseTemplate getMultTemplate() {
        return mult;
    }
}
