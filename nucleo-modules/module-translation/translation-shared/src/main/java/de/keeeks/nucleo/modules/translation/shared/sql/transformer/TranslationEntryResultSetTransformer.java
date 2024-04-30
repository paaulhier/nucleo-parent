package de.keeeks.nucleo.modules.translation.shared.sql.transformer;

import de.keeeks.nucleo.modules.database.sql.statement.ResultSetTransformer;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationEntry;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;
import lombok.RequiredArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

@RequiredArgsConstructor
public class TranslationEntryResultSetTransformer implements ResultSetTransformer<TranslationEntry> {
    private final TranslationApi translationApi;

    @Override
    public TranslationEntry transform(ResultSet resultSet) throws SQLException {
        return new DefaultTranslationEntry(
                resultSet.getInt("id"),
                resultSet.getString("translationKey"),
                locale(resultSet.getString("locale")),
                translationApi.module(resultSet.getInt("moduleId")).orElseThrow(),
                resultSet.getTimestamp("createdAt").toInstant(),
                resultSet.getString("translationValue"),
                resultSet.getTimestamp("updatedAt").toInstant()
        );
    }

    private Locale locale(String localeString) {
        String[] localeParts = localeString.split("_");

        if (localeParts.length == 1) {
            return Locale.of(localeParts[0]);
        } else if (localeParts.length == 2) {
            return Locale.of(localeParts[0], localeParts[1]);
        } else {
            return Locale.of(localeParts[0], localeParts[1], localeParts[2]);
        }
    }
}