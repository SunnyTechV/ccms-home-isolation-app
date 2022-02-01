import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHomeIsolations } from '../home-isolations.model';

@Component({
  selector: 'jhi-home-isolations-detail',
  templateUrl: './home-isolations-detail.component.html',
})
export class HomeIsolationsDetailComponent implements OnInit {
  homeIsolations: IHomeIsolations | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ homeIsolations }) => {
      this.homeIsolations = homeIsolations;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
