/*
 * Copyright 2014, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.sql.postgres;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.annotation.Nullable;

import org.geolatte.geom.Geometry;
import org.geolatte.geom.codec.Wkt;

import com.mysema.query.sql.types.AbstractType;

/**
 * @author tiwe
 *
 */
public class PGGeometryType extends AbstractType<Geometry> {

    public static final PGGeometryType DEFAULT = new PGGeometryType();

    public PGGeometryType() {
        super(Types.STRUCT);
    }

    @Override
    public Class<Geometry> getReturnedClass() {
        return Geometry.class;
    }

    @Override
    @Nullable
    public Geometry getValue(ResultSet rs, int startIndex) throws SQLException {
        String str = rs.getString(startIndex);
        if (str != null) {
            return Wkt.newWktDecoder(Wkt.Dialect.POSTGIS_EWKT_1).decode(str);
        } else {
            return null;
        }
    }

    @Override
    public void setValue(PreparedStatement st, int startIndex, Geometry value) throws SQLException {
        String str = Wkt.newWktEncoder(Wkt.Dialect.POSTGIS_EWKT_1).encode(value);
        st.setString(startIndex, str);
    }

}
