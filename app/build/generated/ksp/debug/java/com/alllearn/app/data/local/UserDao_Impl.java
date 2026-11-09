package com.alllearn.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserEntity> __insertionAdapterOfUserEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateStreak;

  private final SharedSQLiteStatement __preparedStmtOfUpdateTheme;

  public UserDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserEntity = new EntityInsertionAdapter<UserEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `users` (`id`,`email`,`heardAboutUs`,`themeAccent`,`isDarkMode`,`planType`,`streakCount`,`lastStudyDate`,`reminderHour`,`reminderMinute`,`studyDaysJson`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final UserEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getEmail());
        statement.bindString(3, entity.getHeardAboutUs());
        statement.bindString(4, entity.getThemeAccent());
        final int _tmp = entity.isDarkMode() ? 1 : 0;
        statement.bindLong(5, _tmp);
        statement.bindString(6, entity.getPlanType());
        statement.bindLong(7, entity.getStreakCount());
        statement.bindString(8, entity.getLastStudyDate());
        statement.bindLong(9, entity.getReminderHour());
        statement.bindLong(10, entity.getReminderMinute());
        statement.bindString(11, entity.getStudyDaysJson());
      }
    };
    this.__preparedStmtOfUpdateStreak = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET streakCount = ?, lastStudyDate = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateTheme = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE users SET themeAccent = ?, isDarkMode = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertUser(final UserEntity user, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfUserEntity.insert(user);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateStreak(final String userId, final int streak, final String date,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateStreak.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, streak);
        _argIndex = 2;
        _stmt.bindString(_argIndex, date);
        _argIndex = 3;
        _stmt.bindString(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateStreak.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateTheme(final String userId, final String theme, final boolean darkMode,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateTheme.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, theme);
        _argIndex = 2;
        final int _tmp = darkMode ? 1 : 0;
        _stmt.bindLong(_argIndex, _tmp);
        _argIndex = 3;
        _stmt.bindString(_argIndex, userId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfUpdateTheme.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<UserEntity> getUserFlow(final String userId) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"users"}, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHeardAboutUs = CursorUtil.getColumnIndexOrThrow(_cursor, "heardAboutUs");
          final int _cursorIndexOfThemeAccent = CursorUtil.getColumnIndexOrThrow(_cursor, "themeAccent");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final int _cursorIndexOfPlanType = CursorUtil.getColumnIndexOrThrow(_cursor, "planType");
          final int _cursorIndexOfStreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "streakCount");
          final int _cursorIndexOfLastStudyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStudyDate");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfStudyDaysJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studyDaysJson");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpHeardAboutUs;
            _tmpHeardAboutUs = _cursor.getString(_cursorIndexOfHeardAboutUs);
            final String _tmpThemeAccent;
            _tmpThemeAccent = _cursor.getString(_cursorIndexOfThemeAccent);
            final boolean _tmpIsDarkMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp != 0;
            final String _tmpPlanType;
            _tmpPlanType = _cursor.getString(_cursorIndexOfPlanType);
            final int _tmpStreakCount;
            _tmpStreakCount = _cursor.getInt(_cursorIndexOfStreakCount);
            final String _tmpLastStudyDate;
            _tmpLastStudyDate = _cursor.getString(_cursorIndexOfLastStudyDate);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final String _tmpStudyDaysJson;
            _tmpStudyDaysJson = _cursor.getString(_cursorIndexOfStudyDaysJson);
            _result = new UserEntity(_tmpId,_tmpEmail,_tmpHeardAboutUs,_tmpThemeAccent,_tmpIsDarkMode,_tmpPlanType,_tmpStreakCount,_tmpLastStudyDate,_tmpReminderHour,_tmpReminderMinute,_tmpStudyDaysJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getUser(final String userId, final Continuation<? super UserEntity> $completion) {
    final String _sql = "SELECT * FROM users WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<UserEntity>() {
      @Override
      @Nullable
      public UserEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfEmail = CursorUtil.getColumnIndexOrThrow(_cursor, "email");
          final int _cursorIndexOfHeardAboutUs = CursorUtil.getColumnIndexOrThrow(_cursor, "heardAboutUs");
          final int _cursorIndexOfThemeAccent = CursorUtil.getColumnIndexOrThrow(_cursor, "themeAccent");
          final int _cursorIndexOfIsDarkMode = CursorUtil.getColumnIndexOrThrow(_cursor, "isDarkMode");
          final int _cursorIndexOfPlanType = CursorUtil.getColumnIndexOrThrow(_cursor, "planType");
          final int _cursorIndexOfStreakCount = CursorUtil.getColumnIndexOrThrow(_cursor, "streakCount");
          final int _cursorIndexOfLastStudyDate = CursorUtil.getColumnIndexOrThrow(_cursor, "lastStudyDate");
          final int _cursorIndexOfReminderHour = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderHour");
          final int _cursorIndexOfReminderMinute = CursorUtil.getColumnIndexOrThrow(_cursor, "reminderMinute");
          final int _cursorIndexOfStudyDaysJson = CursorUtil.getColumnIndexOrThrow(_cursor, "studyDaysJson");
          final UserEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpEmail;
            _tmpEmail = _cursor.getString(_cursorIndexOfEmail);
            final String _tmpHeardAboutUs;
            _tmpHeardAboutUs = _cursor.getString(_cursorIndexOfHeardAboutUs);
            final String _tmpThemeAccent;
            _tmpThemeAccent = _cursor.getString(_cursorIndexOfThemeAccent);
            final boolean _tmpIsDarkMode;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfIsDarkMode);
            _tmpIsDarkMode = _tmp != 0;
            final String _tmpPlanType;
            _tmpPlanType = _cursor.getString(_cursorIndexOfPlanType);
            final int _tmpStreakCount;
            _tmpStreakCount = _cursor.getInt(_cursorIndexOfStreakCount);
            final String _tmpLastStudyDate;
            _tmpLastStudyDate = _cursor.getString(_cursorIndexOfLastStudyDate);
            final int _tmpReminderHour;
            _tmpReminderHour = _cursor.getInt(_cursorIndexOfReminderHour);
            final int _tmpReminderMinute;
            _tmpReminderMinute = _cursor.getInt(_cursorIndexOfReminderMinute);
            final String _tmpStudyDaysJson;
            _tmpStudyDaysJson = _cursor.getString(_cursorIndexOfStudyDaysJson);
            _result = new UserEntity(_tmpId,_tmpEmail,_tmpHeardAboutUs,_tmpThemeAccent,_tmpIsDarkMode,_tmpPlanType,_tmpStreakCount,_tmpLastStudyDate,_tmpReminderHour,_tmpReminderMinute,_tmpStudyDaysJson);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
