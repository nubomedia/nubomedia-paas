package org.project.openbaton.nubomedia.api.openshift.json;

import com.google.gson.*;
import org.project.openbaton.nubomedia.api.openshift.json.Metadata;
import org.project.openbaton.nubomedia.api.openshift.json.SecretConfig;
import org.project.openbaton.nubomedia.api.openshift.json.SshGitKeySecret;

import java.lang.reflect.Type;

/**
 * Created by maa on 06.10.15.
 */
public class SecretDeserializer implements JsonDeserializer<SecretConfig> {
    @Override
    public SecretConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        //TODO if will be available add other kind of secrets
        JsonObject jsonAsObj = json.getAsJsonObject();
        JsonObject metadata = jsonAsObj.get("metadata").getAsJsonObject();
        Metadata metadata1 = new Metadata(metadata.get("name").getAsString(),"","");
        JsonObject data = jsonAsObj.get("data").getAsJsonObject();
        SshGitKeySecret secret = new SshGitKeySecret(data.get("ssh-primarykey").getAsString());

        return new SecretConfig(metadata1,secret);
    }
}
