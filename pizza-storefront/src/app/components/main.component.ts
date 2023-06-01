import { Component, ElementRef, OnInit, ViewChild, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Order } from '../models';
import { PizzaService } from '../pizza.service';
import { Router } from '@angular/router';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PIZZA_TOPPINGS: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit{

  fb = inject(FormBuilder)
  pSvc = inject(PizzaService)
  router = inject(Router)

  form!: FormGroup
  order!: Order

  pizzaSize = SIZES[0]

  ngOnInit(): void {
      this.form = this.createForm()
  }

  constructor() { }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)]
  }

  createForm() {
    // this.toppingsArray = this.createToppingsArr()
    return this.fb.group({
      name: this.fb.control<string>('Lim Ming Wei, Jerald', [ Validators.required ]),
      email: this.fb.control<string>('jerald.mw.lim@gmail.com', [ Validators.required, Validators.email ]),
      size: this.fb.control<number>(0, [Validators.required, Validators.min(0), Validators.max(3)]),
      base: this.fb.control<string>('', [ Validators.required ]),
      sauce: this.fb.control<string>('', [ Validators.required ]),
      t0: this.fb.control<string>(''),
      t1: this.fb.control<string>(''),
      t2: this.fb.control<string>(''),
      t3: this.fb.control<string>(''),
      t4: this.fb.control<string>(''),
      t5: this.fb.control<string>(''),
      t6: this.fb.control<string>(''),
      comments: this.fb.control<string>('')
    })
  }

  sendOrder(){
    this.order = this.form.value
    this.order.toppings=[]
    for (let i = 0; i < PIZZA_TOPPINGS.length; i++) {
      console.info(this.form.get(`t${i}`)?.value)
      if (!!this.form.get(`t${i}`)?.value)
        this.order.toppings.push(PIZZA_TOPPINGS[i])
    }
    console.info(">>> order: ", this.order)
    this.pSvc.order = this.order
    this.pSvc.placeOrder()
    this.router.navigate(['/', this.order.email])
  }

  hasToppings(){
    for (let i = 0; i < PIZZA_TOPPINGS.length; i++) {
      if (!this.form.get(`t${i}`)?.value)
        return true
    }
    return false
  }

  invalid(){
    return this.form.invalid || !this.hasToppings()
  }
}
