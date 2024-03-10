import { Injectable } from "@angular/core";

export interface Menu {
    state:string;
    name:string;
    type:string;
    role:string;
}

// pages
const MENUITEMS = [
    {state:'dashboard', name:'Dashboard', type:'link', role:''},
    {state:'category', name:'Categories', type:'link', role:'admin'},
    {state:'product', name:'Products', type:'link', role:'admin'},
    {state:'order', name:'Orders', type:'link', role:''},
    {state:'bill', name:'Bills', type:'link', role:''},
    {state:'user', name:'Users', type:'link', role:'admin'}
]

@Injectable()
export class MenuItems{
    getMenuitem():Menu[]{
        return MENUITEMS;
    }
}