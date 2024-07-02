let calendarDate = new Date();
const pointsElement = document.getElementById("points");
let points = localStorage.getItem("points") || 0;
pointsElement.innerText = points;
let attendance = {};

const fetchAttendance = async () => {
  try {
    const response = await fetch(`/get-attendance?userId=${userId}`);
    if (!response.ok) {
      throw new Error('Network response was not ok');
    }
    attendance = await response.json(); // 출석 데이터를 가져옵니다.
    renderCalendar();
  } catch (error) {
    console.error('Failed to fetch attendance data', error);
  }
};

const renderCalendar = () => {
  const viewYear = calendarDate.getFullYear();
  const viewMonth = calendarDate.getMonth();

  document.querySelector('.calendar-year-month').textContent = `${viewYear}년 ${viewMonth + 1}월`;

  // 현재 월의 첫 날과 마지막 날
  const firstDayOfMonth = new Date(viewYear, viewMonth, 1);
  const lastDayOfMonth = new Date(viewYear, viewMonth + 1, 0);

  // 이전 달의 마지막 날
  const lastDayOfPrevMonth = new Date(viewYear, viewMonth, 0);

  const prevDates = [];
  const thisDates = [];
  const nextDates = [];

  // 이전 달 날짜 생성
  const prevMonthDays = lastDayOfPrevMonth.getDate();
  const prevMonthEndDay = lastDayOfPrevMonth.getDay();

  for (let i = prevMonthEndDay; i >= 0; i--) {
    prevDates.push(prevMonthDays - i);
  }

  // 현재 달 날짜 생성
  for (let i = 1; i <= lastDayOfMonth.getDate(); i++) {
    thisDates.push(i);
  }

  // 다음 달 날짜 생성
  const nextMonthDays = 6 - lastDayOfMonth.getDay();
  for (let i = 1; i <= nextMonthDays; i++) {
    nextDates.push(i);
  }

  const dates = [...prevDates, ...thisDates, ...nextDates];

  document.querySelector('.calendar-dates').innerHTML = dates.map((date, i) => {
    let condition;
    let dateKey;

    if (i < prevDates.length) {
      // 이전 달의 날짜 처리
      condition = 'calendar-other';
      dateKey = new Date(viewYear, viewMonth - 1, date).toISOString().split('T')[0];
    } else if (i < prevDates.length + thisDates.length) {
      // 현재 달의 날짜 처리
      condition = 'calendar-this';
      dateKey = new Date(viewYear, viewMonth, date).toISOString().split('T')[0];
    } else {
      // 다음 달의 날짜 처리
      condition = 'calendar-other';
      dateKey = new Date(viewYear, viewMonth + 1, date).toISOString().split('T')[0];
    }

    const isAttendanceChecked = attendance[dateKey] ? 'block' : 'none';

    return `<div class="calendar-date ${condition}" data-date="${dateKey}">
                ${date}
                <img class="calendar-date-stamp" src="/images/Playground/CalendarStamp.png" style="display: ${isAttendanceChecked};"/>
            </div>`;
  }).join('');

  const today = new Date();
  // 오늘 날짜 하이라이트
  if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
    document.querySelectorAll('.calendar-this').forEach(dateElement => {
      if (+dateElement.textContent.trim() === today.getDate()) {
        dateElement.classList.add('calendar-today');
      }
    });
  }
};

const calendarPrevMonth = () => {
  calendarDate.setMonth(calendarDate.getMonth() - 1);
  fetchAttendance(); // 이전 달의 출석 데이터를 다시 가져옵니다.
};

const calendarNextMonth = () => {
  calendarDate.setMonth(calendarDate.getMonth() + 1);
  fetchAttendance(); // 다음 달의 출석 데이터를 다시 가져옵니다.
};

const calendarGoToday = () => {
  calendarDate = new Date();
  fetchAttendance(); // 오늘의 출석 데이터를 다시 가져옵니다.
};

document.addEventListener('DOMContentLoaded', fetchAttendance); // 페이지 로드 시 출석 데이터 가져오기
