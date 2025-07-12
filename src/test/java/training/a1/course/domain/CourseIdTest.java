package training.a1.course.domain;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CourseIdTest {

    @Test
    void shouldCreateCourseIdWithRandomUUID() {
        // when
        CourseId courseId = new CourseId();

        // then
        assertNotNull(courseId);
        assertNotNull(courseId.getId());
    }

    @Test
    void shouldCreateCourseIdWithSpecificUUID() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        CourseId courseId = new CourseId(uuid);

        // then
        assertNotNull(courseId);
        assertEquals(uuid, courseId.getId());
    }

    @Test
    void shouldImplementEqualsAndHashCode() {
        // given
        UUID uuid = UUID.randomUUID();
        CourseId courseId1 = new CourseId(uuid);
        CourseId courseId2 = new CourseId(uuid);
        CourseId courseId3 = new CourseId(UUID.randomUUID());

        // then
        assertEquals(courseId1, courseId2);
        assertEquals(courseId1.hashCode(), courseId2.hashCode());
        assertNotEquals(courseId1, courseId3);
        assertNotEquals(courseId1.hashCode(), courseId3.hashCode());
    }

    @Test
    void shouldImplementToString() {
        // given
        CourseId courseId = new CourseId();

        // when
        String toString = courseId.toString();

        // then
        assertNotNull(toString);
        assertTrue(toString.contains(courseId.getId().toString()));
    }

    @Test
    void shouldNotBeEqualToNull() {
        // given
        CourseId courseId = new CourseId();

        // then
        assertNotEquals(courseId, null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        // given
        CourseId courseId = new CourseId();
        String notACourseId = "not a course id";

        // then
        assertNotEquals(courseId, notACourseId);
    }
}
