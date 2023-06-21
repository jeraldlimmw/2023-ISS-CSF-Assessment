import { Component, OnInit, inject } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { PizzaService } from '../pizza.service';
import { ApiGetResponse } from '../models';
import { Observable, firstValueFrom } from 'rxjs';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit{
  pSvc = inject(PizzaService)
  router = inject(Router)
  activatedRoute = inject(ActivatedRoute)

  email!: string
  orders$!: Observable<ApiGetResponse[]>

  ngOnInit(): void {
    this.email = this.activatedRoute.snapshot.params['email']
    this.orders$ = this.pSvc.getOrders(this.email)
  }

  delivered(orderId: string) {
    firstValueFrom(this.pSvc.delivered(orderId))
      .then(result => {this.orders$ = this.pSvc.getOrders(this.email)})
      .catch(err => {
        alert(JSON.stringify(err))
      })
  }
}
