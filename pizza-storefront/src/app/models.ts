export interface Order {
    t0: string
    t1: string
    t2: string
    t3: string
    t4: string
    t5: string
    t6: string
    name: string
    email: string
    size: string
    base: string
    sauce: string
    toppings: string[]
    comments: string
}

export interface ApiPostResponse {
    orderId: string
    date: number
    name: string
    email: string
    total: number
}

export interface ApiGetResponse {
    orderId: string
    date: number
    name: string
    email: string
    total: number
}