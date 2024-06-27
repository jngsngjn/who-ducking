let calendarDate = new Date();
const pointsElement = document.getElementById("points");
let points = localStorage.getItem("points") || 0;
pointsElement.innerText = points;
let attendance = JSON.parse(localStorage.getItem("attendance")) || {};

const renderCalendar = () => {
  const viewYear = calendarDate.getFullYear();
  const viewMonth = calendarDate.getMonth();

  // year-month 채우기
  document.querySelector('.calendar-year-month').textContent = `${viewYear}년 ${viewMonth + 1}월`;

  // 지난 달 마지막 Date, 이번 달 마지막 Date
  const prevLast = new Date(viewYear, viewMonth, 0);
  const thisLast = new Date(viewYear, viewMonth + 1, 0);

  const PLDate = prevLast.getDate();
  const PLDay = prevLast.getDay();

  const TLDate = thisLast.getDate();
  const TLDay = thisLast.getDay();

  // Dates 기본 배열들
  const prevDates = [];
  const thisDates = [...Array(TLDate + 1).keys()].slice(1);
  const nextDates = [];

  // prevDates 계산
  if (PLDay !== 6) {
    for (let i = 0; i < PLDay + 1; i++) {
      prevDates.unshift(PLDate - i);
    }
  }

  // nextDates 계산
  for (let i = 1; i < 7 - TLDay; i++) {
    nextDates.push(i)
  }

  // Dates 합치기
  const dates = prevDates.concat(thisDates, nextDates);

  // Dates 정리
  const firstDateIndex = dates.indexOf(1);
  const lastDateIndex = dates.lastIndexOf(TLDate);
  dates.forEach((date, i) => {
    const condition = i >= firstDateIndex && i < lastDateIndex + 1
                      ? 'calendar-this'
                      : 'calendar-other';

    dates[i] = `<div class="calendar-date ${condition}">${date}</div>`;
  });

  // Dates 그리기
  document.querySelector('.calendar-dates').innerHTML = dates.join('');

  // 오늘 날짜 그리기
  const today = new Date();
  if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
    for (let date of document.querySelectorAll('.calendar-this')) {
      if (+date.innerText === today.getDate()) {
        date.classList.add('calendar-today');
        break;
      }
    }
  }

  // 출석 체크된 날짜 표시
  document.querySelectorAll('.calendar-date').forEach(date => {
    if (attendance[date.textContent]) {
      date.classList.add('filled');
    }
  });
}

renderCalendar();

const calendarPrevMonth = () => {
  calendarDate.setDate(1);
  calendarDate.setMonth(calendarDate.getMonth() - 1);
  renderCalendar();
}

const calendarNextMonth = () => {
  calendarDate.setDate(1);
  calendarDate.setMonth(calendarDate.getMonth() + 1);
  renderCalendar();
}

const calendarGoToday = () => {
  calendarDate = new Date();
  renderCalendar();
}
