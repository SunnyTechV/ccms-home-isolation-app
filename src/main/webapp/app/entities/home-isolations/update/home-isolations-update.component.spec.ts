import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { HomeIsolationsService } from '../service/home-isolations.service';
import { IHomeIsolations, HomeIsolations } from '../home-isolations.model';
import { IIsolationsDetails } from 'app/entities/isolations-details/isolations-details.model';
import { IsolationsDetailsService } from 'app/entities/isolations-details/service/isolations-details.service';

import { HomeIsolationsUpdateComponent } from './home-isolations-update.component';

describe('HomeIsolations Management Update Component', () => {
  let comp: HomeIsolationsUpdateComponent;
  let fixture: ComponentFixture<HomeIsolationsUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let homeIsolationsService: HomeIsolationsService;
  let isolationsDetailsService: IsolationsDetailsService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [HomeIsolationsUpdateComponent],
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
      .overrideTemplate(HomeIsolationsUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HomeIsolationsUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    homeIsolationsService = TestBed.inject(HomeIsolationsService);
    isolationsDetailsService = TestBed.inject(IsolationsDetailsService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call isolationsDetails query and add missing value', () => {
      const homeIsolations: IHomeIsolations = { id: 456 };
      const isolationsDetails: IIsolationsDetails = { id: 66306 };
      homeIsolations.isolationsDetails = isolationsDetails;

      const isolationsDetailsCollection: IIsolationsDetails[] = [{ id: 91682 }];
      jest.spyOn(isolationsDetailsService, 'query').mockReturnValue(of(new HttpResponse({ body: isolationsDetailsCollection })));
      const expectedCollection: IIsolationsDetails[] = [isolationsDetails, ...isolationsDetailsCollection];
      jest.spyOn(isolationsDetailsService, 'addIsolationsDetailsToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ homeIsolations });
      comp.ngOnInit();

      expect(isolationsDetailsService.query).toHaveBeenCalled();
      expect(isolationsDetailsService.addIsolationsDetailsToCollectionIfMissing).toHaveBeenCalledWith(
        isolationsDetailsCollection,
        isolationsDetails
      );
      expect(comp.isolationsDetailsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const homeIsolations: IHomeIsolations = { id: 456 };
      const isolationsDetails: IIsolationsDetails = { id: 85861 };
      homeIsolations.isolationsDetails = isolationsDetails;

      activatedRoute.data = of({ homeIsolations });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(homeIsolations));
      expect(comp.isolationsDetailsCollection).toContain(isolationsDetails);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HomeIsolations>>();
      const homeIsolations = { id: 123 };
      jest.spyOn(homeIsolationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ homeIsolations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: homeIsolations }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(homeIsolationsService.update).toHaveBeenCalledWith(homeIsolations);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HomeIsolations>>();
      const homeIsolations = new HomeIsolations();
      jest.spyOn(homeIsolationsService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ homeIsolations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: homeIsolations }));
      saveSubject.complete();

      // THEN
      expect(homeIsolationsService.create).toHaveBeenCalledWith(homeIsolations);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<HomeIsolations>>();
      const homeIsolations = { id: 123 };
      jest.spyOn(homeIsolationsService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ homeIsolations });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(homeIsolationsService.update).toHaveBeenCalledWith(homeIsolations);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackIsolationsDetailsById', () => {
      it('Should return tracked IsolationsDetails primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackIsolationsDetailsById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
