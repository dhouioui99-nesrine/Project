import com.example.IntegrationAPI.Base3.Repository.DepRepo;
import com.example.IntegrationAPI.Base3.Repository.EmplRepo;
import com.example.IntegrationAPI.Base3.Service.EmplService;
import com.example.IntegrationAPI.Base3.model.Dep;
import com.example.IntegrationAPI.Base3.model.Empl;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmplServiceTest {



    @Mock
    private EmplRepo emplRepository;

    @Mock
    private DepRepo depRepository;

    @InjectMocks
    private EmplService emplService;

    @Test
    void shouldMigrateEmployeesSuccessfully() {
        // Arrange
        Dep postgresDept = new Dep();
        postgresDept.setName("IT");

        Empl emp1 = new Empl();
        emp1.setEmpCode("E101");
        emp1.setFirstname("Jane");
        emp1.setLastname("Doe");
        emp1.setEmail("jane.doe@example.com");
       

        Empl emp2 = new Empl();
        emp2.setEmpCode("E102");
        emp2.setFirstname("John");
        emp2.setLastname("Smith");
        emp2.setEmail("john.smith@example.com");


        List<Empl> postgresEmployees = Arrays.asList(emp1, emp2);

        when(emplRepository.findAll()).thenReturn(postgresEmployees);

        // Act
        emplService.migrerEmployees();

        // Assert
        verify(emplRepository, times(1)).save(argThat(empl -> empl.getEmpCode().equals("E101")));
        verify(emplRepository, times(1)).save(argThat(empl -> empl.getEmpCode().equals("E102")));
    }

    @Test
    void shouldReturnAllEmployees() {
        // Arrange
        Empl emp1 = new Empl();
        emp1.setEmpCode("E101");
        Empl emp2 = new Empl();
        emp2.setEmpCode("E102");
        List<Empl> expectedEmployees = Arrays.asList(emp1, emp2);
        when(emplRepository.findAll()).thenReturn(expectedEmployees);

        // Act
        List<Empl> actualEmployees = emplService.getAllEmployees();

        // Assert
        assertNotNull(actualEmployees);
        assertEquals(2, actualEmployees.size());
        assertEquals(expectedEmployees, actualEmployees);
        verify(emplRepository, times(1)).findAll();
    }
    
    @Test
    void shouldSaveEmployeeSuccessfully() {
        // Arrange
        Empl newEmpl = new Empl();
        newEmpl.setEmpCode("E103");
        when(emplRepository.save(newEmpl)).thenReturn(newEmpl);

        // Act
        Empl savedEmpl = emplService.save(newEmpl);

        // Assert
        assertNotNull(savedEmpl);
        assertEquals("E103", savedEmpl.getEmpCode());
        verify(emplRepository, times(1)).save(newEmpl);
    }
    
    @Test
    void shouldDeleteEmployeeByEmpCodeWhenFound() {
        // Arrange
        Empl empToDelete = new Empl();
        empToDelete.setEmpCode("E101");
        List<Empl> foundEmployees = Arrays.asList(empToDelete);
        when(emplRepository.findByEmpCode("E101")).thenReturn(foundEmployees);

        // Act
        emplService.deleteByEmpCode("E101");

        // Assert
        verify(emplRepository, times(1)).findByEmpCode("E101");
        verify(emplRepository, times(1)).delete(empToDelete);
    }

    @Test
    void shouldThrowExceptionWhenDeleteByEmpCodeAndNotFound() {
        // Arrange
        when(emplRepository.findByEmpCode("E999")).thenReturn(Arrays.asList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emplService.deleteByEmpCode("E999");
        });

        assertEquals("No employee found with empCode: E999", exception.getMessage());
        verify(emplRepository, times(1)).findByEmpCode("E999");
        verify(emplRepository, never()).delete(any(Empl.class));
    }

    @Test
    void shouldUpdateEmployeeByEmpCodeWhenFound() {
        // Arrange
        Empl existingEmpl = new Empl();
        existingEmpl.setEmpCode("E101");
        existingEmpl.setFirstname("Old");
        
        Empl updatedData = new Empl();
        updatedData.setFirstname("New");
        updatedData.setDepartment("Marketing");

        when(emplRepository.findFirstByEmpCode("E101")).thenReturn(Optional.of(existingEmpl));
        when(emplRepository.save(any(Empl.class))).thenReturn(existingEmpl);

        // Act
        Empl result = emplService.updateByEmpCode("E101", updatedData);

        // Assert
        assertNotNull(result);
        assertEquals("New", result.getFirstname());
        assertEquals("Marketing", result.getDepartment());
        verify(emplRepository, times(1)).findFirstByEmpCode("E101");
        verify(emplRepository, times(1)).save(any(Empl.class));
    }

    @Test
    void shouldThrowExceptionWhenUpdateByEmpCodeAndNotFound() {
        // Arrange
        Empl updatedData = new Empl();
        when(emplRepository.findFirstByEmpCode("E999")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            emplService.updateByEmpCode("E999", updatedData);
        });

        assertEquals("employe not found with empCode: E999", exception.getMessage());
        verify(emplRepository, times(1)).findFirstByEmpCode("E999");
        verify(emplRepository, never()).save(any(Empl.class));
    }
    
    @Test
    void shouldReturnEmployeeListWhenGetEmployeeByIdIsCalled() {
        // Arrange
        Empl emp1 = new Empl();
        emp1.setEmpCode("E101");
        List<Empl> foundEmployees = Arrays.asList(emp1);
        when(emplRepository.findByEmpCode("E101")).thenReturn(foundEmployees);

        // Act
        List<Empl> result = emplService.getEmployeeById("E101");

        // Assert
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals("E101", result.get(0).getEmpCode());
        verify(emplRepository, times(1)).findByEmpCode("E101");
    }
}