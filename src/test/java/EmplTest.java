import com.example.IntegrationAPI.Base3.model.Empl;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmplTest {

    @Test
    void testEmplGettersAndSetters() {
        // Arrange
        Long id = 1L;
        String empCode = "EMP001";
        String firstname = "Jane";
        String lastname = "Doe";
        String email = "jane.doe@example.com";
        String department = "IT";

        // Act
        Empl empl = new Empl();
        empl.setId(id);
        empl.setEmpCode(empCode);
        empl.setFirstname(firstname);
        empl.setLastname(lastname);
        empl.setEmail(email);
        empl.setDepartment(department);

        // Assert
        assertEquals(id, empl.getId());
        assertEquals(empCode, empl.getEmpCode());
        assertEquals(firstname, empl.getFirstname());
        assertEquals(lastname, empl.getLastname());
        assertEquals(email, empl.getEmail());
        assertEquals(department, empl.getDepartment());
    }

    @Test
    void testEmplConstructor() {
        // Arrange
        Long id = 2L;
        String empCode = "EMP002";
        String firstname = "John";
        String lastname = "Smith";
        String email = "john.smith@example.com";
        String department = "HR";

        // Act
        Empl empl = new Empl(id, empCode, firstname, lastname, email, department);

        // Assert
        assertEquals(id, empl.getId());
        assertEquals(empCode, empl.getEmpCode());
        assertEquals(firstname, empl.getFirstname());
        assertEquals(lastname, empl.getLastname());
        assertEquals(email, empl.getEmail());
        assertEquals(department, empl.getDepartment());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        Empl empl1 = new Empl(1L, "EMP001", "Jane", "Doe", "jane.doe@example.com", "IT");
        Empl empl2 = new Empl(1L, "EMP001", "Jane", "Doe", "jane.doe@example.com", "IT");
        Empl empl3 = new Empl(2L, "EMP002", "John", "Smith", "john.smith@example.com", "HR");

        // Act & Assert
        assertEquals(empl1, empl2); // Same object values should be equal
        assertEquals(empl1.hashCode(), empl2.hashCode()); // Same hash code

        assertNotEquals(empl1, empl3); // Different objects should not be equal
        assertNotEquals(empl1.hashCode(), empl3.hashCode()); // Different hash codes
    }
}