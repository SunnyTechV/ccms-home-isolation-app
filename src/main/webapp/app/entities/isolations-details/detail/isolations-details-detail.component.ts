import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIsolationsDetails } from '../isolations-details.model';

@Component({
  selector: 'jhi-isolations-details-detail',
  templateUrl: './isolations-details-detail.component.html',
})
export class IsolationsDetailsDetailComponent implements OnInit {
  isolationsDetails: IIsolationsDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ isolationsDetails }) => {
      this.isolationsDetails = isolationsDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
