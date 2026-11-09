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
import java.lang.Long;
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
public final class LessonDao_Impl implements LessonDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<LessonEntity> __insertionAdapterOfLessonEntity;

  private final SharedSQLiteStatement __preparedStmtOfUpdateLessonStatus;

  private final SharedSQLiteStatement __preparedStmtOfSaveQuizResult;

  public LessonDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfLessonEntity = new EntityInsertionAdapter<LessonEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `lessons` (`id`,`courseId`,`weekNumber`,`title`,`minutes`,`learningGoalsJson`,`contentMarkdown`,`sourcesJson`,`quizJson`,`status`,`quizScore`,`quizTotal`,`lastVerified`,`needsReview`,`completedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final LessonEntity entity) {
        statement.bindString(1, entity.getId());
        statement.bindString(2, entity.getCourseId());
        statement.bindLong(3, entity.getWeekNumber());
        statement.bindString(4, entity.getTitle());
        statement.bindLong(5, entity.getMinutes());
        statement.bindString(6, entity.getLearningGoalsJson());
        statement.bindString(7, entity.getContentMarkdown());
        statement.bindString(8, entity.getSourcesJson());
        statement.bindString(9, entity.getQuizJson());
        statement.bindString(10, entity.getStatus());
        statement.bindLong(11, entity.getQuizScore());
        statement.bindLong(12, entity.getQuizTotal());
        statement.bindString(13, entity.getLastVerified());
        final int _tmp = entity.getNeedsReview() ? 1 : 0;
        statement.bindLong(14, _tmp);
        if (entity.getCompletedAt() == null) {
          statement.bindNull(15);
        } else {
          statement.bindLong(15, entity.getCompletedAt());
        }
      }
    };
    this.__preparedStmtOfUpdateLessonStatus = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE lessons SET status = ?, completedAt = ? WHERE id = ?";
        return _query;
      }
    };
    this.__preparedStmtOfSaveQuizResult = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE lessons SET quizScore = ?, quizTotal = ?, status = 'done', completedAt = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertLesson(final LessonEntity lesson,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLessonEntity.insert(lesson);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertLessons(final List<LessonEntity> lessons,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfLessonEntity.insert(lessons);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateLessonStatus(final String lessonId, final String status,
      final Long completedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfUpdateLessonStatus.acquire();
        int _argIndex = 1;
        _stmt.bindString(_argIndex, status);
        _argIndex = 2;
        if (completedAt == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindLong(_argIndex, completedAt);
        }
        _argIndex = 3;
        _stmt.bindString(_argIndex, lessonId);
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
          __preparedStmtOfUpdateLessonStatus.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Object saveQuizResult(final String lessonId, final int score, final int total,
      final long completedAt, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfSaveQuizResult.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, score);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, total);
        _argIndex = 3;
        _stmt.bindLong(_argIndex, completedAt);
        _argIndex = 4;
        _stmt.bindString(_argIndex, lessonId);
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
          __preparedStmtOfSaveQuizResult.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<LessonEntity>> getLessonsForCourse(final String courseId) {
    final String _sql = "SELECT * FROM lessons WHERE courseId = ? ORDER BY weekNumber ASC, id ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, courseId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"lessons"}, new Callable<List<LessonEntity>>() {
      @Override
      @NonNull
      public List<LessonEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "minutes");
          final int _cursorIndexOfLearningGoalsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "learningGoalsJson");
          final int _cursorIndexOfContentMarkdown = CursorUtil.getColumnIndexOrThrow(_cursor, "contentMarkdown");
          final int _cursorIndexOfSourcesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcesJson");
          final int _cursorIndexOfQuizJson = CursorUtil.getColumnIndexOrThrow(_cursor, "quizJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuizScore = CursorUtil.getColumnIndexOrThrow(_cursor, "quizScore");
          final int _cursorIndexOfQuizTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "quizTotal");
          final int _cursorIndexOfLastVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVerified");
          final int _cursorIndexOfNeedsReview = CursorUtil.getColumnIndexOrThrow(_cursor, "needsReview");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<LessonEntity> _result = new ArrayList<LessonEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final LessonEntity _item;
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCourseId;
            _tmpCourseId = _cursor.getString(_cursorIndexOfCourseId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpMinutes;
            _tmpMinutes = _cursor.getInt(_cursorIndexOfMinutes);
            final String _tmpLearningGoalsJson;
            _tmpLearningGoalsJson = _cursor.getString(_cursorIndexOfLearningGoalsJson);
            final String _tmpContentMarkdown;
            _tmpContentMarkdown = _cursor.getString(_cursorIndexOfContentMarkdown);
            final String _tmpSourcesJson;
            _tmpSourcesJson = _cursor.getString(_cursorIndexOfSourcesJson);
            final String _tmpQuizJson;
            _tmpQuizJson = _cursor.getString(_cursorIndexOfQuizJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuizScore;
            _tmpQuizScore = _cursor.getInt(_cursorIndexOfQuizScore);
            final int _tmpQuizTotal;
            _tmpQuizTotal = _cursor.getInt(_cursorIndexOfQuizTotal);
            final String _tmpLastVerified;
            _tmpLastVerified = _cursor.getString(_cursorIndexOfLastVerified);
            final boolean _tmpNeedsReview;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNeedsReview);
            _tmpNeedsReview = _tmp != 0;
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new LessonEntity(_tmpId,_tmpCourseId,_tmpWeekNumber,_tmpTitle,_tmpMinutes,_tmpLearningGoalsJson,_tmpContentMarkdown,_tmpSourcesJson,_tmpQuizJson,_tmpStatus,_tmpQuizScore,_tmpQuizTotal,_tmpLastVerified,_tmpNeedsReview,_tmpCompletedAt);
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
  public Object getLessonById(final String lessonId,
      final Continuation<? super LessonEntity> $completion) {
    final String _sql = "SELECT * FROM lessons WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, lessonId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<LessonEntity>() {
      @Override
      @Nullable
      public LessonEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "minutes");
          final int _cursorIndexOfLearningGoalsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "learningGoalsJson");
          final int _cursorIndexOfContentMarkdown = CursorUtil.getColumnIndexOrThrow(_cursor, "contentMarkdown");
          final int _cursorIndexOfSourcesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcesJson");
          final int _cursorIndexOfQuizJson = CursorUtil.getColumnIndexOrThrow(_cursor, "quizJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuizScore = CursorUtil.getColumnIndexOrThrow(_cursor, "quizScore");
          final int _cursorIndexOfQuizTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "quizTotal");
          final int _cursorIndexOfLastVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVerified");
          final int _cursorIndexOfNeedsReview = CursorUtil.getColumnIndexOrThrow(_cursor, "needsReview");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final LessonEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCourseId;
            _tmpCourseId = _cursor.getString(_cursorIndexOfCourseId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpMinutes;
            _tmpMinutes = _cursor.getInt(_cursorIndexOfMinutes);
            final String _tmpLearningGoalsJson;
            _tmpLearningGoalsJson = _cursor.getString(_cursorIndexOfLearningGoalsJson);
            final String _tmpContentMarkdown;
            _tmpContentMarkdown = _cursor.getString(_cursorIndexOfContentMarkdown);
            final String _tmpSourcesJson;
            _tmpSourcesJson = _cursor.getString(_cursorIndexOfSourcesJson);
            final String _tmpQuizJson;
            _tmpQuizJson = _cursor.getString(_cursorIndexOfQuizJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuizScore;
            _tmpQuizScore = _cursor.getInt(_cursorIndexOfQuizScore);
            final int _tmpQuizTotal;
            _tmpQuizTotal = _cursor.getInt(_cursorIndexOfQuizTotal);
            final String _tmpLastVerified;
            _tmpLastVerified = _cursor.getString(_cursorIndexOfLastVerified);
            final boolean _tmpNeedsReview;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNeedsReview);
            _tmpNeedsReview = _tmp != 0;
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new LessonEntity(_tmpId,_tmpCourseId,_tmpWeekNumber,_tmpTitle,_tmpMinutes,_tmpLearningGoalsJson,_tmpContentMarkdown,_tmpSourcesJson,_tmpQuizJson,_tmpStatus,_tmpQuizScore,_tmpQuizTotal,_tmpLastVerified,_tmpNeedsReview,_tmpCompletedAt);
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
  public Flow<LessonEntity> getLessonFlowById(final String lessonId) {
    final String _sql = "SELECT * FROM lessons WHERE id = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, lessonId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"lessons"}, new Callable<LessonEntity>() {
      @Override
      @Nullable
      public LessonEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfCourseId = CursorUtil.getColumnIndexOrThrow(_cursor, "courseId");
          final int _cursorIndexOfWeekNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "weekNumber");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfMinutes = CursorUtil.getColumnIndexOrThrow(_cursor, "minutes");
          final int _cursorIndexOfLearningGoalsJson = CursorUtil.getColumnIndexOrThrow(_cursor, "learningGoalsJson");
          final int _cursorIndexOfContentMarkdown = CursorUtil.getColumnIndexOrThrow(_cursor, "contentMarkdown");
          final int _cursorIndexOfSourcesJson = CursorUtil.getColumnIndexOrThrow(_cursor, "sourcesJson");
          final int _cursorIndexOfQuizJson = CursorUtil.getColumnIndexOrThrow(_cursor, "quizJson");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfQuizScore = CursorUtil.getColumnIndexOrThrow(_cursor, "quizScore");
          final int _cursorIndexOfQuizTotal = CursorUtil.getColumnIndexOrThrow(_cursor, "quizTotal");
          final int _cursorIndexOfLastVerified = CursorUtil.getColumnIndexOrThrow(_cursor, "lastVerified");
          final int _cursorIndexOfNeedsReview = CursorUtil.getColumnIndexOrThrow(_cursor, "needsReview");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final LessonEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpId;
            _tmpId = _cursor.getString(_cursorIndexOfId);
            final String _tmpCourseId;
            _tmpCourseId = _cursor.getString(_cursorIndexOfCourseId);
            final int _tmpWeekNumber;
            _tmpWeekNumber = _cursor.getInt(_cursorIndexOfWeekNumber);
            final String _tmpTitle;
            _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            final int _tmpMinutes;
            _tmpMinutes = _cursor.getInt(_cursorIndexOfMinutes);
            final String _tmpLearningGoalsJson;
            _tmpLearningGoalsJson = _cursor.getString(_cursorIndexOfLearningGoalsJson);
            final String _tmpContentMarkdown;
            _tmpContentMarkdown = _cursor.getString(_cursorIndexOfContentMarkdown);
            final String _tmpSourcesJson;
            _tmpSourcesJson = _cursor.getString(_cursorIndexOfSourcesJson);
            final String _tmpQuizJson;
            _tmpQuizJson = _cursor.getString(_cursorIndexOfQuizJson);
            final String _tmpStatus;
            _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            final int _tmpQuizScore;
            _tmpQuizScore = _cursor.getInt(_cursorIndexOfQuizScore);
            final int _tmpQuizTotal;
            _tmpQuizTotal = _cursor.getInt(_cursorIndexOfQuizTotal);
            final String _tmpLastVerified;
            _tmpLastVerified = _cursor.getString(_cursorIndexOfLastVerified);
            final boolean _tmpNeedsReview;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfNeedsReview);
            _tmpNeedsReview = _tmp != 0;
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new LessonEntity(_tmpId,_tmpCourseId,_tmpWeekNumber,_tmpTitle,_tmpMinutes,_tmpLearningGoalsJson,_tmpContentMarkdown,_tmpSourcesJson,_tmpQuizJson,_tmpStatus,_tmpQuizScore,_tmpQuizTotal,_tmpLastVerified,_tmpNeedsReview,_tmpCompletedAt);
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
