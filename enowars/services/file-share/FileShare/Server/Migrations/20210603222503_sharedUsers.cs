using System.Collections.Generic;
using Microsoft.EntityFrameworkCore.Migrations;

namespace FileShare.Server.Migrations
{
    public partial class sharedUsers : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<List<string>>(
                name: "SharedWithUsers",
                table: "AspNetUsers",
                type: "text[]",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "SharedWithUsers",
                table: "AspNetUsers");
        }
    }
}
