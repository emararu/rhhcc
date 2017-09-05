package com.rhhcc.common.cache;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.rhhcc.common.type.DBResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Класс реализующий кэширование справочника текстовых сообщений
 * 
 * @author EMararu
 * @version 0.00.01
 */
@Service("cacheMessage")
public class CacheMessage implements Cache {
    
    private final Logger log = LoggerFactory.getLogger(CacheMessage.class);
    
    @Autowired
    @Qualifier("dataSource")
    private BasicDataSource ds;
    
    @Cacheable("messages")
    @Override
    public DBResult get(long key) {
                
        log.info("key=" + key);
        
        DBResult result;
        
        try (Connection con = ds.getConnection()) {
                        
            // По переданному идентификатору возвращает текст сообщения с учетом подстановочной строки
            String sql = "{ call msg(?, ?, ?, ?) }";
            log.info(sql);
            CallableStatement prep = con.prepareCall(sql);

            // IN
            // * Идентификатор cообщения
            prep.setLong(1, key);
            // * Подстановочная строка в cообщении
            prep.setNull(2, java.sql.Types.VARCHAR);

            // OUT
            // * Статус информирующий об успешности вызова основой процедуры
            prep.registerOutParameter(3, java.sql.Types.INTEGER);
            // * Текст сообщения
            prep.registerOutParameter(4, java.sql.Types.VARCHAR);

            prep.execute();

            result = new DBResult(prep.getLong(3), prep.getString(4));
            
        } catch (SQLException e) {
            log.info("SQL:"+e.getMessage());
            result = new DBResult(-600, e.getMessage());
        } catch (Exception e) {
            log.info("Error:" + e.toString());
            result = new DBResult(-600, e.toString());
        }
        log.info(result.toString());
        
        return result;
    }
        
    @CacheEvict(value = "messages", allEntries=true)
    @Override
    public void clear() {
    }
    
}
