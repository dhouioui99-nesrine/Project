import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Empl } from '../Model/Empl.model';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root'
})
export class EmplService {

  private baseURL = "http://localhost:9000/api/empl";
 
   constructor(private httpClient: HttpClient) { }
   
   getEmployeeList(): Observable<Empl[]>{
     return this.httpClient.get<Empl[]>(`${this.baseURL}`);
   }

 

  // Fonction pour ajouter un employé
  addEmployee(employee: Empl): Observable<Empl> {
    return this.httpClient.post<Empl>(this.baseURL, employee);
  }
  // 🔄 Update un employé
  updateEmployee(empCode: string, employee: Empl): Observable<Empl> {
    return this.httpClient.put<Empl>(`${this.baseURL}/${empCode}`, employee);
  }

  // 🗑️ Supprimer un employé
  deleteEmployee(empCode: string): Observable<void> {
    return this.httpClient.delete<void>(`${this.baseURL}/${empCode}`);
  }

  getEmployeeByCode(empCode: string): Observable<any> {
  return this.httpClient.get(`${this.baseURL}/${empCode}`);
}

}
