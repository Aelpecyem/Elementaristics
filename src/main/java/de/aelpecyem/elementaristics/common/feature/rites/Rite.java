package de.aelpecyem.elementaristics.common.feature.rites;


import de.aelpecyem.elementaristics.common.entity.EntityNexus;
import de.aelpecyem.elementaristics.common.feature.alchemy.AspectAttunement;
import net.minecraft.util.Identifier;

public abstract class Rite {
    //do render stuff now that the rite base is there
    //rite selection stuff (based on Attunement, maybe even Nexus Aspects (still need to be added)
    /*
        Nexus color will be based on the last aspect added (Rite Tools add color)
        Once the Rite is activated, the color will be determined by the rite

        the potential of the nexus determines its brightness, also its size
     */
    public final Identifier NAME;
    private final int EXECUTION_TIME;
    private final AspectAttunement ATTUNEMENT;
    private final int COLOR;

    public Rite(Identifier name, int executionTime, AspectAttunement attunement, int color) {
        NAME = name;
        EXECUTION_TIME = executionTime;
        ATTUNEMENT = attunement;
        COLOR = color;
    }

    public boolean canFinish(EntityNexus nexus) {
        return true;
    }

    public abstract void tick(EntityNexus nexus);

    public abstract void finish(EntityNexus nexus);


    public int getExecutionTime(EntityNexus nexus) {
        return EXECUTION_TIME;
    }

    public int getColor(EntityNexus nexus) {
        return COLOR;
    }
}
