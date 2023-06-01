import { HttpClient } from "@angular/common/http"
import { Injectable, inject } from "@angular/core"
import { ApiGetResponse, ApiPostResponse, Order } from "./models"

@Injectable()
export class PizzaService {

  http = inject(HttpClient)

  order!: Order

  // TODO: Task 3
  // You may add any parameters and return any type from placeOrder() method
  // Do not change the method name
  placeOrder() {
    console.info("service order: ", this.order)
    return this.http.post<ApiPostResponse>('/api/order', this.order)
  }

  // TODO: Task 5
  // You may add any parameters and return any type from getOrders() method
  // Do not change the method name
  getOrders(email: string) {
    return this.http.get<ApiGetResponse[]>(`/api/orders/${email}`)
  }

  // TODO: Task 7
  // You may add any parameters and return any type from delivered() method
  // Do not change the method name
  delivered(orderId: string) {
    return this.http.delete<string>(`/api/order/${orderId}`)
  }

}
