package de.keeeks.nucleo.modules.translation.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.translation.shared.DefaultTranslationEntry;
import de.keeeks.nucleo.modules.translation.shared.sql.transformer.TranslationEntryResultSetTransformer;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import de.keeeks.nucleo.modules.translations.api.TranslationApi;
import de.keeeks.nucleo.modules.translations.api.TranslationEntry;

import java.util.List;
import java.util.Locale;

public class TranslationEntryRepository {

    private final MysqlConnection mysqlConnection;

    private final TranslationEntryResultSetTransformer transformer;

    public TranslationEntryRepository(TranslationApi translationApi, MysqlConnection mysqlConnection) {
        this.mysqlConnection = mysqlConnection;
        transformer = new TranslationEntryResultSetTransformer(translationApi);
    }

    public List<TranslationEntry> translationEntries() {
        return mysqlConnection.queryList(
                "select * from translations;",
                transformer
        );
    }

    public List<TranslationEntry> translationEntries(ModuleDetails moduleDetails) {
        return mysqlConnection.queryList(
                "select * from translations where moduleId = ?;",
                statement -> statement.setInt(1, moduleDetails.id()),
                transformer
        );
    }

    public TranslationEntry createTranslationEntry(ModuleDetails moduleDetails, Locale locale, String key, String value) {
        int id = mysqlConnection.keyInsert(
                "insert into translations (moduleId, translationKey, translationValue, locale) values (?, ?, ?, ?);",
                statement -> {
                    statement.setInt(1, moduleDetails.id());
                    statement.setString(2, key);
                    statement.setString(3, value);
                    statement.setString(4, locale.toLanguageTag());
                }
        );
        return new DefaultTranslationEntry(
                id,
                key,
                locale,
                moduleDetails,
                value
        );
    }

    public void updateTranslationEntry(TranslationEntry translationEntry) {
        mysqlConnection.prepare(
                "update translations set translationValue = ? where id = ?;",
                statement -> {
                    statement.setString(1, translationEntry.value());
                    statement.setInt(2, translationEntry.id());
                }
        );
    }

    public void deleteTranslationEntry(TranslationEntry translationEntry) {
        mysqlConnection.prepare(
                "delete from translations where id = ?;",
                statement -> statement.setInt(1, translationEntry.id())
        );
    }
}