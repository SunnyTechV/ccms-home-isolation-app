import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IsolationsDetailsService } from '../service/isolations-details.service';
import { IIsolationsDetails, IsolationsDetails } from '../isolations-details.model';

import { IsolationsDetailsUpdateComponent } from './isolations-details-update.component';

describe('IsolationsDetails Management Update Component', () => {
  let comp: IsolationsDetailsUpdateComponent;
  let fixture: ComponentFixture<IsolationsDetailsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let isolationsDetailsService: IsolationsDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [IsolationsDetailsUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(IsolationsDetailsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(IsolationsDetailsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    isolationsDetailsService = TestBed.inject(IsolationsDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const isolationsDetails: IIsolationsDetails = { id: 456 };

      activatedRoute.data = of({ isolationsDetails });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(isolationsDetails));
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsolationsDetails>>();
      const isolationsDetails = { id: 123 };
      jest.spyOn(isolationsDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isolationsDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isolationsDetails }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(isolationsDetailsService.update).toHaveBeenCalledWith(isolationsDetails);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsolationsDetails>>();
      const isolationsDetails = new IsolationsDetails();
      jest.spyOn(isolationsDetailsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isolationsDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: isolationsDetails }));
      saveSubject.complete();

      // THEN
      expect(isolationsDetailsService.create).toHaveBeenCalledWith(isolationsDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IsolationsDetails>>();
      const isolationsDetails = { id: 123 };
      jest.spyOn(isolationsDetailsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ isolationsDetails });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(isolationsDetailsService.update).toHaveBeenCalledWith(isolationsDetails);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
