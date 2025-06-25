package com.svincent7.sentraiam.common.auth.token;

import com.svincent7.sentraiam.common.dto.credential.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.json.JSONObject;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@ToString(callSuper = true)
public class SentraClaims extends DefaultClaims implements Claims {
    public SentraClaims(final Claims claims, final JSONObject jsonObject) {
        super(claims);

        this.setValue(TokenConstant.ACTIVE, true);
        this.setValue(TokenConstant.USERNAME, jsonObject.getString(TokenConstant.USERNAME));
        this.setValue(TokenConstant.VERSION, jsonObject.getInt(TokenConstant.VERSION));
        this.setValue(TokenConstant.TENANT_ID, jsonObject.getString(TokenConstant.TENANT_ID));
        if (jsonObject.has(TokenConstant.FIRSTNAME)) {
            this.setValue(TokenConstant.FIRSTNAME, jsonObject.getString(TokenConstant.FIRSTNAME));
        }
        if (jsonObject.has(TokenConstant.LASTNAME)) {
            this.setValue(TokenConstant.LASTNAME, jsonObject.getString(TokenConstant.LASTNAME));
        }
        this.setValue("scope", "tenants:create tenants:update");
    }
}
