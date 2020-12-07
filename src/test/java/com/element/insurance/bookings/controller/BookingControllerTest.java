package com.element.insurance.bookings.controller;

import com.element.insurance.bookings.dto.*;
import com.element.insurance.bookings.entity.TrailEntity;
import com.element.insurance.bookings.service.TrailService;
import com.element.insurance.bookings.service.impl.BookingTrailServiceImpl;
import com.element.insurance.bookings.util.TestUtil;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@WebMvcTest(value = BookingController.class)
@ContextConfiguration(classes = {BookingController.class,
        GlobalExceptionHandler.class})
public class BookingControllerTest {

    private static final String BOOKING_URL = "/v1/bookings/";

    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext webApplicationContext;
    @MockBean
    private BookingTrailServiceImpl bookingTrailService;
    @MockBean
    private TrailService trailService;

    @Before
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .build();
    }

    @Test
    public void testGet() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        bookingDtos.add(TestUtil.bookingDto());
        Mockito.when(bookingTrailService.get(Mockito.anyString(), Mockito.any(LocalDate.class)))
                .thenReturn(bookingDtos);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(BOOKING_URL).param("bookingDate", "1992-06-26")
                .param("userEmail", "ajay@gmail.com"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        JavaType type = objectMapper.getTypeFactory().constructCollectionType(List.class,
                Class.forName(bookingDtos.get(0).getClass().getName()));
        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), type),
                Matchers.equalTo(bookingDtos));
    }

    @Test
    public void testGetWithDate() throws Exception {
        List<BookingDto> bookingDtos = new ArrayList<>();
        BookingDto bookingDto = TestUtil.bookingDto();
        bookingDto.setBookingDate(LocalDate.parse("1992-06-26"));
        bookingDtos.add(bookingDto);
        PageableResponse<BookingDto> pageableResponse = new PageableResponse<>(bookingDtos.size(), 1,
                bookingDtos);
        Mockito.when(bookingTrailService.getByDate(Mockito.anyString(), Mockito.any(Pageable.class)))
                .thenReturn(pageableResponse);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get(String.join("", BOOKING_URL, "/1992-06-26")))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assert.assertNotNull(mvcResult);

        JavaType type = objectMapper.getTypeFactory().constructParametricType(PageableResponse.class,
                Class.forName(bookingDtos.get(0).getClass().getName()));
        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), type),
                Matchers.equalTo(pageableResponse));
    }

    @Test
    public void testPost() throws Exception {

        bookingDtoValidationSetup();

        Mockito.when(bookingTrailService.create(Mockito.any(BookingCreateUpdateDto.class)))
                .thenReturn(TestUtil.bookingDto());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .post(BOOKING_URL).content(objectMapper.writeValueAsString(TestUtil.bookingCreateUpdateDto()))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        Assert.assertNotNull(mvcResult);

        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class),
                Matchers.equalTo(TestUtil.bookingDto()));
    }

    private void bookingDtoValidationSetup() {
        Map<TrailsEnum, TrailEntity> trails = new HashMap<>();
        trails.put(TrailsEnum.Shire, TestUtil.trailEntity());
        Mockito.when(trailService.getTrails()).thenReturn(trails);
    }

    @Test
    public void testInvalidTimeLimitPost() throws Exception {

        bookingDtoValidationSetup();
        BookingCreateUpdateDto bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        bookingCreateUpdateDto.setStartTime(LocalTime.of(5, 00, 00));

        mockMvc.perform(MockMvcRequestBuilders
                .post(BOOKING_URL).content(objectMapper.writeValueAsString(bookingCreateUpdateDto))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Mockito.verify(bookingTrailService, Mockito.times(0)).create(Mockito.any(BookingCreateUpdateDto.class));
    }

    @Test
    public void testInvalidAgeLimitPost() throws Exception {

        bookingDtoValidationSetup();
        BookingCreateUpdateDto bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        UserDto userDto = TestUtil.userDto();
        userDto.setDob(LocalDate.now());
        bookingCreateUpdateDto.setBookedBy(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post(BOOKING_URL).content(objectMapper.writeValueAsString(bookingCreateUpdateDto))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Mockito.verify(bookingTrailService, Mockito.times(0)).create(Mockito.any(BookingCreateUpdateDto.class));
    }


    @Test
    public void testDuplicateEmailPost() throws Exception {

        bookingDtoValidationSetup();
        BookingCreateUpdateDto bookingCreateUpdateDto = TestUtil.bookingCreateUpdateDto();
        UserDto userDto = TestUtil.userDto();
        bookingCreateUpdateDto.getHikers().add(userDto);

        mockMvc.perform(MockMvcRequestBuilders
                .post(BOOKING_URL).content(objectMapper.writeValueAsString(bookingCreateUpdateDto))
                .accept(MediaType.APPLICATION_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn();

        Mockito.verify(bookingTrailService, Mockito.times(0)).create(Mockito.any(BookingCreateUpdateDto.class));
    }

    @Test
    public void testUpdateStatus() throws Exception {

        BookingDto bookingDto = TestUtil.bookingDto();
        bookingDto.setBookingStatus(BookingStatus.Canceled);
        Mockito.when(bookingTrailService.updateStatus(Mockito.any(BookingSearchDto.class), Mockito.any(BookingStatus.class)))
                .thenReturn(bookingDto);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(String.join("", BOOKING_URL, BookingStatus.Canceled.name()))
                .content(objectMapper.writeValueAsString(TestUtil.bookingSearchDto())).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        System.out.println(mvcResult.getResponse().getContentAsString());
        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class),
                Matchers.equalTo(bookingDto));
    }

    @Test
    public void testUpdate() throws Exception {
        bookingDtoValidationSetup();
        BookingDto bookingDto = TestUtil.bookingDto();
        bookingDto.setBookingStatus(BookingStatus.Canceled);
        Mockito.when(bookingTrailService.update(Mockito.any(BookingCreateUpdateDto.class)))
                .thenReturn(bookingDto);

        BookingCreateUpdateDto updateDto = TestUtil.bookingCreateUpdateDto();
        updateDto.setBookingStatus(BookingStatus.Canceled);

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .put(BOOKING_URL)
                .content(objectMapper.writeValueAsString(updateDto)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        Assert.assertNotNull(mvcResult);
        Assert.assertThat(objectMapper.readValue(mvcResult.getResponse().getContentAsString(), BookingDto.class),
                Matchers.equalTo(bookingDto));
    }

    @Test
    public void testDelete() throws Exception {
        bookingDtoValidationSetup();
        Mockito.doNothing().when(bookingTrailService).delete(Mockito.any(BookingSearchDto.class));
        mockMvc.perform(MockMvcRequestBuilders
                .delete(BOOKING_URL)
                .content(objectMapper.writeValueAsString(TestUtil.bookingSearchDto())).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andReturn();


    }

    @Profile("embedded")
    @SpringBootApplication
    public static class Config {

    }

}
