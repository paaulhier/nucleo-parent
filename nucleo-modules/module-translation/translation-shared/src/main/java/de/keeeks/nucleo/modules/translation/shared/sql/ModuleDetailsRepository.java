package de.keeeks.nucleo.modules.translation.shared.sql;

import de.keeeks.nucleo.modules.database.sql.MysqlConnection;
import de.keeeks.nucleo.modules.translation.shared.DefaultModuleDetails;
import de.keeeks.nucleo.modules.translation.shared.sql.transformer.ModuleDetailsResulSetTransformer;
import de.keeeks.nucleo.modules.translations.api.ModuleDetails;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

@RequiredArgsConstructor
public class ModuleDetailsRepository {
    private final ModuleDetailsResulSetTransformer transformer = new ModuleDetailsResulSetTransformer();

    private final MysqlConnection mysqlConnection;

    public List<ModuleDetails> moduleDetails() {
        return mysqlConnection.queryList(
                "select * from modules",
                transformer
        );
    }

    public ModuleDetails module(int id) {
        return mysqlConnection.query(
                "select * from modules where id = ?",
                statement -> statement.setInt(1, id),
                transformer
        );
    }

    public ModuleDetails createModule(String name) {
        int id = mysqlConnection.keyInsert(
                "insert into modules (name) values (?)",
                statement -> statement.setString(1, name)
        );
        return new DefaultModuleDetails(
                id,
                name,
                Instant.now(),
                Instant.now()
        );
    }

    public void updateModule(ModuleDetails moduleDetails) {
        mysqlConnection.prepare(
                "update modules set name = ? where id = ?",
                statement -> {
                    statement.setString(1, moduleDetails.name());
                    statement.setInt(2, moduleDetails.id());
                }
        );
    }

    public void deleteModule(ModuleDetails moduleDetails) {
        mysqlConnection.prepare(
                "delete from modules where id = ?",
                statement -> statement.setInt(1, moduleDetails.id())
        );
    }
}