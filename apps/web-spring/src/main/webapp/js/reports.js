function reportsDropdown() {
    document.getElementById("reports-dropdown").classList.toggle("show");
}

window.onclick = function(event) {
    if (!event.target.matches('.report-button') &&
        !event.target.matches('.report-button-icon') &&
        !event.target.matches('.report-button-text')
    ) {
        var dropdowns = document.getElementsByClassName("report-button-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
          var openDropdown = dropdowns[i];
          if (openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
          }
        }
    }

    if (!event.target.matches('.menu-dropdown')) {
        var dropdowns = document.getElementsByClassName("user-dropdown-content");
        var i;
        for (i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                  if (openDropdown.classList.contains('show')) {
                    openDropdown.classList.remove('show');
                  }
        }
    }
}