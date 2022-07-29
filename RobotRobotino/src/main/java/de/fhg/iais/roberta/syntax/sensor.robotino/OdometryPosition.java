package de.fhg.iais.roberta.syntax.sensor.robotino;

import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoHide;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.util.syntax.WithUserDefinedPort;

@NepoPhrase(category = "SENSOR", blocklyNames = {"robSensors_odometry_getSample"}, name = "ODOMETRY_SENSING")
public class OdometryPosition extends Sensor implements WithUserDefinedPort {
    @NepoField(name = BlocklyConstants.SENSORPORT, value = BlocklyConstants.EMPTY_PORT)
    public final String port;
    @NepoField(name = BlocklyConstants.SLOT, value = BlocklyConstants.EMPTY_SLOT)
    public final String slot;
    @NepoHide
    public final Hide hide;

    public OdometryPosition(BlocklyProperties properties, String port, String slot, Hide hide) {
        super(properties);
        Assert.nonEmptyString(port);
        this.port = port;
        this.slot = slot;
        this.hide = hide;
        setReadOnly();
    }

    @Override
    public String getUserDefinedPort() {
        return this.port;
    }
}
