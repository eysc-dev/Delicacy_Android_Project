package com.yschang.delicacy.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.yschang.delicacy.entity.MyUser;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MyUser> __insertionAdapterOfMyUser;

  private final EntityDeletionOrUpdateAdapter<MyUser> __updateAdapterOfMyUser;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMyUser = new EntityInsertionAdapter<MyUser>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `MyUser` (`id`,`username`,`fullName`,`phoneNumber`,`password`,`email`,`code`,`cart`,`history`,`favorite`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyUser entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUsername());
        statement.bindString(3, entity.getFullName());
        statement.bindString(4, entity.getPhoneNumber());
        statement.bindString(5, entity.getPassword());
        statement.bindString(6, entity.getEmail());
        statement.bindString(7, entity.getCode());
        statement.bindString(8, entity.getCart());
        statement.bindString(9, entity.getHistory());
        statement.bindString(10, entity.getFavorite());
      }
    };
    this.__updateAdapterOfMyUser = new EntityDeletionOrUpdateAdapter<MyUser>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `MyUser` SET `id` = ?,`username` = ?,`fullName` = ?,`phoneNumber` = ?,`password` = ?,`email` = ?,`code` = ?,`cart` = ?,`history` = ?,`favorite` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MyUser entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getUsername());
        statement.bindString(3, entity.getFullName());
        statement.bindString(4, entity.getPhoneNumber());
        statement.bindString(5, entity.getPassword());
        statement.bindString(6, entity.getEmail());
        statement.bindString(7, entity.getCode());
        statement.bindString(8, entity.getCart());
        statement.bindString(9, entity.getHistory());
        statement.bindString(10, entity.getFavorite());
        statement.bindLong(11, entity.getId());
      }
    };
  }

  @Override
  public void insertUser(final MyUser user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfMyUser.insert(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateUser(final MyUser user) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfMyUser.handle(user);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public MyUser findByUsername(final String username) {
    final String _sql = "select * from myuser where username = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, username);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfUsername = CursorUtil.getColumnIndexOrThrow(_cursor, "username");
      final int _cursorIndexOfFullName = CursorUtil.getColumnIndexOrThrow(_cursor, "fullName");
      final int _cursorIndexOfPhoneNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "phoneNumber");
      final int _cursorIndexOfPassword = CursorUtil.getColumnIndexOrThrow(_cursor, "password");
      final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
      final int _cursorIndexOfCode = CursorUtil.getColumnIndexOrThrow(_cursor, "code");
      final int _cursorIndexOfCart = CursorUtil.getColumnIndexOrThrow(_cursor, "cart");
      final int _cursorIndexOfHistory = CursorUtil.getColumnIndexOrThrow(_cursor, "history");
      final int _cursorIndexOfFavorite = CursorUtil.getColumnIndexOrThrow(_cursor, "favorite");
      final MyUser _result;
      if (_cursor.moveToFirst()) {
        final int _tmpId;
        _tmpId = _cursor.getInt(_cursorIndexOfId);
        final String _tmpUsername;
        _tmpUsername = _cursor.getString(_cursorIndexOfUsername);
        final String _tmpFullName;
        _tmpFullName = _cursor.getString(_cursorIndexOfFullName);
        final String _tmpPhoneNumber;
        _tmpPhoneNumber = _cursor.getString(_cursorIndexOfPhoneNumber);
        final String _tmpPassword;
        _tmpPassword = _cursor.getString(_cursorIndexOfPassword);
        final String _tmpEmail;
        _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
        final String _tmpCode;
        _tmpCode = _cursor.getString(_cursorIndexOfCode);
        final String _tmpCart;
        _tmpCart = _cursor.getString(_cursorIndexOfCart);
        final String _tmpHistory;
        _tmpHistory = _cursor.getString(_cursorIndexOfHistory);
        final String _tmpFavorite;
        _tmpFavorite = _cursor.getString(_cursorIndexOfFavorite);
        _result = new MyUser(_tmpId,_tmpUsername,_tmpFullName,_tmpPhoneNumber,_tmpPassword,_tmpEmail,_tmpCode,_tmpCart,_tmpHistory,_tmpFavorite);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
