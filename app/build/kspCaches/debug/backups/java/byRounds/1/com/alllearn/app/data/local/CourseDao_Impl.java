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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class CourseDao_Impl implements CourseDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<CourseEntity> __insertionAdapterOfCourseEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCourseOutline;

  private final SharedSQLiteStatement __preparedStmtOfUpdateCourseStatus;

  private final SharedSQLiteStatement __preparedStmtOfDeleteCourse;

  public CourseDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfCourseEntity = new EntityInsertionAdapter<CourseEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `courses` (`id`,`userId`,`area`,`topic`,`levelKey`,`levelNote`,`deadlineWeeks`,`hoursPerWeek`,`targetEndDate`,`status`,`outlineJson`,`regenerationsUsed`,`createdAt`,`updatedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final CourseEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getUserId());
        statement.bindString(3, entity.getArea());
        statement.bindString(4, entity.getTopic());
        statement.bindString(5, entity.getLevelKey());
        statement.bindString(6, entity.getLevelNote());
        statement.bindLong(7, entity.getDeadlineWeeks());
        statement.bindLong(8, entity.getHoursPerWeek());
        statement.bindString(9, entity.getTargetEndDate());
        statement.bindString(10, entity.getStatus());
        statement.bindString(11, entity.getOutlineJson());
        statement.bindLong(12, entity.getRegenerationsUsed());
        statement.bindLong(13, entity.getCreatedAt());
        statement.bindLong(14, entity.getUpdatedAt());
      }
    };
    this.__preparedStmtOfUpdateCourseOutline = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE courses SET outlineJson = ?, updatedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfUpdateCourseStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE courses SET status = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfDeleteCourse = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM courses WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertCourse(final CourseEntity course,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfCourseEntity.insert(course);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCourseOutline(final String courseId, final String outlineJson,
      final long updatedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCourseOutline.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, outlineJson);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, updatedAt);
        _argIndex = 3;
        _stmt.bindString(_argIndex, courseId);
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
          __preparedStmtOfUpdateCourseOutline.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object updateCourseStatus(final String courseId, final String status,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateCourseStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        _stmt.bindString(_argIndex, courseId);
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
          __preparedStmtOfUpdateCourseStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteCourse(final String courseId, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteCourse.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, courseId);
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
          __preparedStmtOfDeleteCourse.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<CourseEntity>> getCoursesForUser(final String userId) {
    final String _sql = "SELECT * FROM courses WHERE userId = ? ORDER BY updatedAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, userId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"courses"}, new Callable<List<CourseEntity>>() {
      @Override
      @NonNull
      public List<CourseEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfLevelKey = CursorUtil.getColumnIndexOrThrow(_cursor, "levelKey");
          final int _cursorIndexOfLevelNote = CursorUtil.getColumnIndexOrThrow(_cursor, "levelNote");
          final int _cursorIndexOfDeadlineWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "deadlineWeeks");
          final int _cursorIndexOfHoursPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "hoursPerWeek");
          final int _cursorIndexOfTargetEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOutlineJson = CursorUtil.getColumnIndexOrThrow(_cursor, "outlineJson");
          final int _cursorIndexOfRegenerationsUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "regenerationsUsed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final List<CourseEntity> _result = new ArrayList<CourseEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final CourseEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpArea;
            _tmpArea = _cursor.getString(_cursorIndexOfArea);
            final String _tmpTopic;
            _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            final String _tmpLevelKey;
            _tmpLevelKey = _cursor.getString(_cursorIndexOfLevelKey);
            final String _tmpLevelNote;
            _tmpLevelNote = _cursor.getString(_cursorIndexOfLevelNote);
            final int _tmpDeadlineWeeks;
            _tmpDeadlineWeeks = _cursor.getInt(_cursorIndexOfDeadlineWeeks);
            final int _tmpHoursPerWeek;
            _tmpHoursPerWeek = _cursor.getInt(_cursorIndexOfHoursPerWeek);
            final String _tmpTargetEndDate;
            _tmpTargetEndDate = _cursor.getString(_cursorIndexOfTargetEndDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpOutlineJson;
            _tmpOutlineJson = _cursor.getString(_cursorIndexOfOutlineJson);
            final int _tmpRegenerationsUsed;
            _tmpRegenerationsUsed = _cursor.getInt(_cursorIndexOfRegenerationsUsed);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _item = new CourseEntity(_tmpId,_tmpUserId,_tmpArea,_tmpTopic,_tmpLevelKey,_tmpLevelNote,_tmpDeadlineWeeks,_tmpHoursPerWeek,_tmpTargetEndDate,_tmpStatus,_tmpOutlineJson,_tmpRegenerationsUsed,_tmpCreatedAt,_tmpUpdatedAt);
            _result.add(_item);
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
  public Object getCourseById(final String courseId,
      final Continuation<? super CourseEntity> $completion) {
    final String _sql = "SELECT * FROM courses WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, courseId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<CourseEntity>() {
      @Override
      @Nullable
      public CourseEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfLevelKey = CursorUtil.getColumnIndexOrThrow(_cursor, "levelKey");
          final int _cursorIndexOfLevelNote = CursorUtil.getColumnIndexOrThrow(_cursor, "levelNote");
          final int _cursorIndexOfDeadlineWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "deadlineWeeks");
          final int _cursorIndexOfHoursPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "hoursPerWeek");
          final int _cursorIndexOfTargetEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOutlineJson = CursorUtil.getColumnIndexOrThrow(_cursor, "outlineJson");
          final int _cursorIndexOfRegenerationsUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "regenerationsUsed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CourseEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpArea;
            _tmpArea = _cursor.getString(_cursorIndexOfArea);
            final String _tmpTopic;
            _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            final String _tmpLevelKey;
            _tmpLevelKey = _cursor.getString(_cursorIndexOfLevelKey);
            final String _tmpLevelNote;
            _tmpLevelNote = _cursor.getString(_cursorIndexOfLevelNote);
            final int _tmpDeadlineWeeks;
            _tmpDeadlineWeeks = _cursor.getInt(_cursorIndexOfDeadlineWeeks);
            final int _tmpHoursPerWeek;
            _tmpHoursPerWeek = _cursor.getInt(_cursorIndexOfHoursPerWeek);
            final String _tmpTargetEndDate;
            _tmpTargetEndDate = _cursor.getString(_cursorIndexOfTargetEndDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpOutlineJson;
            _tmpOutlineJson = _cursor.getString(_cursorIndexOfOutlineJson);
            final int _tmpRegenerationsUsed;
            _tmpRegenerationsUsed = _cursor.getInt(_cursorIndexOfRegenerationsUsed);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CourseEntity(_tmpId,_tmpUserId,_tmpArea,_tmpTopic,_tmpLevelKey,_tmpLevelNote,_tmpDeadlineWeeks,_tmpHoursPerWeek,_tmpTargetEndDate,_tmpStatus,_tmpOutlineJson,_tmpRegenerationsUsed,_tmpCreatedAt,_tmpUpdatedAt);
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

  @Override
  public Flow<CourseEntity> getCourseFlowById(final String courseId) {
    final String _sql = "SELECT * FROM courses WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, courseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"courses"}, new Callable<CourseEntity>() {
      @Override
      @Nullable
      public CourseEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfUserId = CursorUtil.getColumnIndexOrThrow(_cursor, "userId");
          final int _cursorIndexOfArea = CursorUtil.getColumnIndexOrThrow(_cursor, "area");
          final int _cursorIndexOfTopic = CursorUtil.getColumnIndexOrThrow(_cursor, "topic");
          final int _cursorIndexOfLevelKey = CursorUtil.getColumnIndexOrThrow(_cursor, "levelKey");
          final int _cursorIndexOfLevelNote = CursorUtil.getColumnIndexOrThrow(_cursor, "levelNote");
          final int _cursorIndexOfDeadlineWeeks = CursorUtil.getColumnIndexOrThrow(_cursor, "deadlineWeeks");
          final int _cursorIndexOfHoursPerWeek = CursorUtil.getColumnIndexOrThrow(_cursor, "hoursPerWeek");
          final int _cursorIndexOfTargetEndDate = CursorUtil.getColumnIndexOrThrow(_cursor, "targetEndDate");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfOutlineJson = CursorUtil.getColumnIndexOrThrow(_cursor, "outlineJson");
          final int _cursorIndexOfRegenerationsUsed = CursorUtil.getColumnIndexOrThrow(_cursor, "regenerationsUsed");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final CourseEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpUserId;
            _tmpUserId = _cursor.getString(_cursorIndexOfUserId);
            final String _tmpArea;
            _tmpArea = _cursor.getString(_cursorIndexOfArea);
            final String _tmpTopic;
            _tmpTopic = _cursor.getString(_cursorIndexOfTopic);
            final String _tmpLevelKey;
            _tmpLevelKey = _cursor.getString(_cursorIndexOfLevelKey);
            final String _tmpLevelNote;
            _tmpLevelNote = _cursor.getString(_cursorIndexOfLevelNote);
            final int _tmpDeadlineWeeks;
            _tmpDeadlineWeeks = _cursor.getInt(_cursorIndexOfDeadlineWeeks);
            final int _tmpHoursPerWeek;
            _tmpHoursPerWeek = _cursor.getInt(_cursorIndexOfHoursPerWeek);
            final String _tmpTargetEndDate;
            _tmpTargetEndDate = _cursor.getString(_cursorIndexOfTargetEndDate);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final String _tmpOutlineJson;
            _tmpOutlineJson = _cursor.getString(_cursorIndexOfOutlineJson);
            final int _tmpRegenerationsUsed;
            _tmpRegenerationsUsed = _cursor.getInt(_cursorIndexOfRegenerationsUsed);
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            _result = new CourseEntity(_tmpId,_tmpUserId,_tmpArea,_tmpTopic,_tmpLevelKey,_tmpLevelNote,_tmpDeadlineWeeks,_tmpHoursPerWeek,_tmpTargetEndDate,_tmpStatus,_tmpOutlineJson,_tmpRegenerationsUsed,_tmpCreatedAt,_tmpUpdatedAt);
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

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
