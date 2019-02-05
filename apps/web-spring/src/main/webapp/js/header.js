
function dropDownUserMenu() {
    document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown if the user clicks outside of it
window.onclick = function(event) {
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

function setMenuActivated(menuId) {
  var element = document.getElementById(menuId);
  element.classList.add("menu-item-selected");
  var elText = element.getElementsByClassName("menu-item-text");
  elText[0].classList.add("menu-item-text-selected");
}
